package com.furia.knowyourfan.services.IAServices;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OcrService {

    private final ITesseract tesseract;

    public OcrService() {
        tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata"); // Caminho correto do tessdata
        tesseract.setLanguage("eng"); // Idioma configurado como inglês (ajuste para o idioma necessário)
    }

    public String extrairTexto(MultipartFile file) throws IOException, TesseractException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new IOException("Erro ao ler a imagem: Formato inválido ou imagem corrompida.");
        }
        return tesseract.doOCR(image); // Faz a extração de texto da imagem
    }

    public DadosExtraidos extrairDados(String texto) {
        String cpf = extrairCpf(texto);
        String dataNascimento = extrairDataNascimento(texto);
        String nome = extrairNomeCompleto(texto);
    
        return new DadosExtraidos(nome, cpf, dataNascimento);
    }
    
    // Ajuste na Regex do Nome Completo
    private String extrairNomeCompleto(String texto) {
        // Regex para capturar o nome completo entre os caracteres "<<" e ">>"
        Pattern pattern = Pattern.compile("<<\\s*([a-zA-Z\\s]+(?:\\s+[a-zA-Z\\s]+)*)\\s*>>");
        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            return matcher.group(1).trim();  // Retorna o nome completo encontrado
        }
        return null;
    }
    
    // Ajuste na Regex do CPF (permitindo fragmentos do CPF com caracteres diferentes)
    private String extrairCpf(String texto) {
        // Regex para CPF com ou sem pontuação, mais flexível
        Pattern pattern = Pattern.compile("(\\d{3}[\\.\\-\\s]?\\d{3}[\\.\\-\\s]?\\d{3}[\\-\\s]?\\d{2})");
        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            return matcher.group().replaceAll("[^\\d]", "");  // Remover caracteres não numéricos
        }
        return null;
    }
    
    // Ajuste na Regex da Data de Nascimento
    private String extrairDataNascimento(String texto) {
        // Regex para capturar datas no formato dd/mm/yyyy ou dd-mm-yyyy
        Pattern pattern = Pattern.compile("(\\d{2}[\\/\\-]\\d{2}[\\/\\-]\\d{4})");
        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
    // Classe para armazenar os dados extraídos
    public static class DadosExtraidos {
        private String nome;
        private String cpf;
        private String dataNascimento;

        public DadosExtraidos(String nome, String cpf, String dataNascimento) {
            this.nome = nome;
            this.cpf = cpf;
            this.dataNascimento = dataNascimento;
        }

        public String getNome() {
            return nome;
        }

        public String getCpf() {
            return cpf;
        }

        public String getDataNascimento() {
            return dataNascimento;
        }

        @Override
        public String toString() {
            return "Nome: " + nome + "\nCPF: " + cpf + "\nData de Nascimento: " + dataNascimento;
        }
    }
}
