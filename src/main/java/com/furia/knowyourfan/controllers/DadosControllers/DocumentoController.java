package com.furia.knowyourfan.controllers.DadosControllers;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.furia.knowyourfan.services.IAServices.OcrService;
import com.furia.knowyourfan.services.IAServices.OcrService.DadosExtraidos;

import jakarta.servlet.http.HttpServletRequest;

import com.furia.knowyourfan.model.Entitys.User;
import com.furia.knowyourfan.repositories.UserRepository;

import java.io.IOException;


@Controller
public class DocumentoController {

    @Autowired
    private OcrService ocrService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String formularioUpload() {
        return "upload";  // Página de upload do arquivo
    }

    @PostMapping("/analisar")
    public String analisarDocumento(@RequestParam("file") MultipartFile file, HttpServletRequest request, Model model) {
        try {
            // Extrai o texto do documento usando OCR
            String textoExtraido = ocrService.extrairTexto(file);

            // Imprime o texto extraído para debug
            System.out.println("Texto Extraído: " + textoExtraido);

            // Extrai os dados (nome, CPF, data de nascimento)
            DadosExtraidos dadosExtraidos = ocrService.extrairDados(textoExtraido);

            // Armazena os dados extraídos na sessão
            request.getSession().setAttribute("dados", dadosExtraidos);

            // Passa os dados extraídos para a página de confirmação
            model.addAttribute("dados", dadosExtraidos);

            return "confirmar";  // Exibe os dados extraídos na página de confirmação
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao processar o arquivo: " + e.getMessage());
            return "upload";  // Volta para a página de upload em caso de erro
        }
    }

    @PostMapping("/confirmar")
    public String confirmarDocumento(@RequestParam("confirmado") boolean confirmado,
                                      HttpServletRequest request, Model model) {
        User usuario = (User) request.getSession().getAttribute("user");
    
        if (usuario == null) {
            model.addAttribute("erro", "Usuário não encontrado na sessão.");
            return "upload";
        }
    
        if (confirmado) {
            DadosExtraidos dados = (DadosExtraidos) request.getSession().getAttribute("dados");
    
            if (dados != null) {
                usuario.setNome(dados.getNome());
                usuario.setCpf(dados.getCpf());
                usuario.setDataNascimento(dados.getDataNascimento());
    
                userRepository.save(usuario);
    
                model.addAttribute("mensagem", "Documento confirmado com sucesso! Dados salvos.");
    
                // ✅ Redireciona para a próxima etapa (conectar redes sociais)
                return "redirect:/redes-sociais";
            } else {
                model.addAttribute("erro", "Dados extraídos não encontrados.");
                return "upload";
            }
        } else {
            model.addAttribute("mensagem", "Documento rejeitado. Por favor, tente novamente.");
            return "upload";
        }
    }
    
}
