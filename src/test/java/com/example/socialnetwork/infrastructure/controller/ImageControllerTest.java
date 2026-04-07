package com.example.socialnetwork.infrastructure.controller;

import com.example.socialnetwork.application.service.FileStorageService;
import com.example.socialnetwork.infrastructure.security.JwtTokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ImageController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private FileStorageService fileStorageService;
    @MockBean private UserDetailsService userDetailsService;
    @MockBean private JwtTokenUtil jwtTokenUtil;

    @Test
    @DisplayName("Deve baixar a imagem com sucesso")
    @WithMockUser
    void shouldServeFileSuccessfully() throws Exception {
        String filename = "profile-pic.jpg";
        Resource mockResource = new ByteArrayResource("conteudo-da-imagem".getBytes()) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        when(fileStorageService.load(filename)).thenReturn(mockResource);

        mockMvc.perform(get("/api/v1/images/" + filename))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"profile-pic.jpg\""))
                .andExpect(content().string("conteudo-da-imagem"));
    }
}