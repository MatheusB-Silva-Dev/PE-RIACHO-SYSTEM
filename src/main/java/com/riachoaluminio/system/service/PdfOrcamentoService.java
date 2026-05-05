package com.riachoaluminio.system.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.riachoaluminio.system.entity.Orcamento;
import com.riachoaluminio.system.entity.ItemOrcamento;
import org.springframework.beans.factory.annotation.Value;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PdfOrcamentoService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    // Cores baseadas na identidade visual da logo Riacho Alumínio
    private static final BaseColor COR_PRIMARIA   = new BaseColor(13, 27, 75);    // Azul navy escuro (texto da logo)
    private static final BaseColor COR_SECUNDARIA = new BaseColor(26, 75, 224);   // Azul royal/elétrico (símbolo da logo)
    private static final BaseColor COR_PRATA      = new BaseColor(138, 155, 176); // Prata metálico (estrutura da casa)
    private static final BaseColor COR_LINHA_PAR  = new BaseColor(230, 235, 248); // Azul bem claro (zebra das linhas)
    private static final BaseColor BRANCO         = BaseColor.WHITE;

    public byte[] gerarPdf(Orcamento orcamento) throws DocumentException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // --- Logo ---
        try {

            InputStream logoStream = PdfOrcamentoService.class.getResourceAsStream("/static/logo.png");
            Image logo = Image.getInstance(logoStream.readAllBytes());

            float desiredWidth = 250f;
            float proportion = logo.getWidth() / logo.getHeight();
            float desiredHeight = desiredWidth / proportion;

            logo.scaleAbsolute(desiredWidth, desiredHeight);
            logo.setAlignment(Element.ALIGN_CENTER);

            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);

            PdfPCell cellLogo = new PdfPCell(logo, false);
            cellLogo.setBorder(PdfPCell.NO_BORDER);
            cellLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellLogo.setPaddingBottom(8f);

            headerTable.addCell(cellLogo);

            document.add(headerTable);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // --- Linha separadora (azul royal) ---
        PdfPTable linhaSeparadora = new PdfPTable(1);
        linhaSeparadora.setWidthPercentage(100);
        PdfPCell linhaCel = new PdfPCell(new Phrase(" "));
        linhaCel.setBorder(PdfPCell.NO_BORDER);
        linhaCel.setBorderWidthBottom(2f);
        linhaCel.setBorderColorBottom(COR_SECUNDARIA);
        linhaCel.setPaddingBottom(4f);
        linhaSeparadora.addCell(linhaCel);
        document.add(linhaSeparadora);
        document.add(new Paragraph(" "));

        // --- Subtítulo (azul navy) ---
        Font fonteSubtitulo = new Font(Font.FontFamily.HELVETICA, 11, Font.ITALIC, COR_PRIMARIA);
        Paragraph subtitulo = new Paragraph("Fabricação e instalação de esquadrias (alumínio e vidro)", fonteSubtitulo);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitulo);
        document.add(new Paragraph(" "));

        // --- Dados do Cliente ---
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy");
        Font fonteDadosLabel = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, COR_SECUNDARIA); // azul royal
        Font fonteDadosValor = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        PdfPTable tabelaDados = new PdfPTable(3);
        tabelaDados.setWidthPercentage(100);
        tabelaDados.setWidths(new float[]{40, 30, 30});
        tabelaDados.setSpacingAfter(10f);
        tabelaDados.addCell(criarCelulaDados("Cliente: ", orcamento.getCliente().getNome(), fonteDadosLabel, fonteDadosValor));
        tabelaDados.addCell(criarCelulaDados("Data: ", orcamento.getDataCriacao().format(formatter), fonteDadosLabel, fonteDadosValor));
        tabelaDados.addCell(criarCelulaDados("Local: ", "Brasília", fonteDadosLabel, fonteDadosValor));
        document.add(tabelaDados);

        // --- Tabela de Itens ---
        PdfPTable tabelaItens = new PdfPTable(8);
        tabelaItens.setWidthPercentage(100);
        tabelaItens.setSpacingBefore(6f);
        tabelaItens.setSpacingAfter(10f);
        tabelaItens.setWidths(new float[]{8, 22, 15, 8, 8, 6, 12, 12});

        Font fonteCabTabela = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BRANCO);
        addTableHeader(tabelaItens, "Foto", fonteCabTabela);
        addTableHeader(tabelaItens, "Modelo", fonteCabTabela);
        addTableHeader(tabelaItens, "Material", fonteCabTabela);
        addTableHeader(tabelaItens, "Largura", fonteCabTabela);
        addTableHeader(tabelaItens, "Altura", fonteCabTabela);
        addTableHeader(tabelaItens, "Qtd", fonteCabTabela);
        addTableHeader(tabelaItens, "Valor", fonteCabTabela);
        addTableHeader(tabelaItens, "Total", fonteCabTabela);

        Font fonteCelula = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        DecimalFormat df = new DecimalFormat("#,##0.00");

        List<ItemOrcamento> itens = orcamento.getItens();
        for (int i = 0; i < itens.size(); i++) {
            ItemOrcamento item = itens.get(i);
            BaseColor corLinha = (i % 2 == 0) ? BRANCO : COR_LINHA_PAR;

            String imagemUrl = item.getProduto().getImagemUrl();
            if (imagemUrl != null && !imagemUrl.isEmpty()) {
                try {
                    String nomeArquivo = imagemUrl.replace("C:/riacho-uploads/produtos/", "");
                    String caminhoAbsoluto = uploadDir + "/" + nomeArquivo;
                    Image imgProduto = Image.getInstance(caminhoAbsoluto);
                    imgProduto.scaleAbsolute(40f, 40f);
                    PdfPCell celulaImagem = new PdfPCell(imgProduto, false);
                    celulaImagem.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celulaImagem.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celulaImagem.setPadding(2f);
                    celulaImagem.setBackgroundColor(corLinha);
                    tabelaItens.addCell(celulaImagem);
                } catch (Exception e) {
                    tabelaItens.addCell(criarCelulaColorida("—", fonteCelula, corLinha));
                }
            } else {
                tabelaItens.addCell(criarCelulaColorida("—", fonteCelula, corLinha));
            }

            tabelaItens.addCell(criarCelulaColorida(item.getProduto().getModelo(), fonteCelula, corLinha));
            tabelaItens.addCell(criarCelulaColorida(item.getProduto().getMaterial(), fonteCelula, corLinha));
            tabelaItens.addCell(criarCelulaColorida(String.valueOf(item.getLargura()), fonteCelula, corLinha));
            tabelaItens.addCell(criarCelulaColorida(String.valueOf(item.getAltura()), fonteCelula, corLinha));
            tabelaItens.addCell(criarCelulaColorida(String.valueOf(item.getQuantidade()), fonteCelula, corLinha));
            tabelaItens.addCell(criarCelulaColorida("R$ " + df.format(item.getValorUnitario()), fonteCelula, corLinha));
            tabelaItens.addCell(criarCelulaColorida("R$ " + df.format(item.getValorUnitario().multiply(
                    BigDecimal.valueOf(item.getQuantidade()))), fonteCelula, corLinha));
        }
        document.add(tabelaItens);

        // --- Dados Adicionais ---
        Font fonteDados = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
        Paragraph infoAdicional = new Paragraph();
        infoAdicional.setFont(fonteDados);
        infoAdicional.add(new Phrase("Prazo de entrega: " + orcamento.getPrazoEntrega() + " dias úteis     "));
        infoAdicional.add(new Phrase("Vendedor: " + orcamento.getVendedor() + "     "));
        infoAdicional.add(new Phrase("Forma de Pagamento: " + orcamento.getFormaPagamento()));
        document.add(infoAdicional);
        document.add(new Paragraph(" "));

        // --- Total em caixa destacada ---
        PdfPTable tabelaTotal = new PdfPTable(1);
        tabelaTotal.setWidthPercentage(30);
        tabelaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);

        // Header "TOTAL DO ORÇAMENTO" → fundo azul navy
        Font fonteTotalLabel = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BRANCO);
        PdfPCell celulaLabelTotal = new PdfPCell(new Phrase("TOTAL DO ORÇAMENTO", fonteTotalLabel));
        celulaLabelTotal.setBackgroundColor(COR_PRIMARIA);
        celulaLabelTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaLabelTotal.setPadding(6f);
        celulaLabelTotal.setBorder(PdfPCell.NO_BORDER);
        tabelaTotal.addCell(celulaLabelTotal);

        // Valor → fonte azul royal, borda prata metálico
        Font fonteTotalValor = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, COR_SECUNDARIA);
        PdfPCell celulaValorTotal = new PdfPCell(new Phrase("R$ " + df.format(orcamento.getValorTotal()), fonteTotalValor));
        celulaValorTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaValorTotal.setPadding(8f);
        celulaValorTotal.setBorderColor(COR_PRATA);
        tabelaTotal.addCell(celulaValorTotal);
        document.add(tabelaTotal);
        document.add(new Paragraph(" "));

        // --- Rodapé (prata metálico) ---
        Font fonteRodape = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, COR_PRATA);
        Paragraph rodape = new Paragraph("Orçamento válido por 05 dias", fonteRodape);
        rodape.setAlignment(Element.ALIGN_CENTER);
        document.add(rodape);

        document.close();
        return outputStream.toByteArray();
    }

    private PdfPCell criarCelulaDados(String label, String valor, Font fonteLabel, Font fonteValor) {
        Phrase phrase = new Phrase();
        phrase.add(new Chunk(label, fonteLabel));
        phrase.add(new Chunk(valor, fonteValor));
        PdfPCell cel = new PdfPCell(phrase);
        cel.setBorder(PdfPCell.NO_BORDER);
        cel.setPadding(3f);
        return cel;
    }

    private PdfPCell criarCelulaColorida(String texto, Font fonte, BaseColor cor) {
        PdfPCell celula = new PdfPCell(new Phrase(texto, fonte));
        celula.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celula.setPadding(3f);
        celula.setBackgroundColor(cor);
        return celula;
    }

    private PdfPCell criarCelula(String texto, Font fonte) {
        PdfPCell celula = new PdfPCell(new Phrase(texto, fonte));
        celula.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celula.setPadding(3f);
        return celula;
    }

    private void addTableHeader(PdfPTable tabela, String headerTitle, Font fonte) {
        PdfPCell headerCell = new PdfPCell(new Phrase(headerTitle, fonte));
        headerCell.setBackgroundColor(COR_SECUNDARIA); // azul royal (símbolo da logo)
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(6f);
        tabela.addCell(headerCell);
    }
}