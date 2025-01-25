package com.kn.wedding.manager.controller;

import com.kn.wedding.manager.dto.BasicGuestView;
import com.kn.wedding.manager.handler.WeddingManagerHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class WeddingManagerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WeddingManagerHandler managerHandler;

    @Test
    @WithMockUser(username = "example", roles = "GUEST")
    void getGuestListAndExpectSuccessResultAssertContent() throws Exception {
        var randomGuestCode1 = UUID.randomUUID();
        var randomGuestCode2 = UUID.randomUUID();

        List<BasicGuestView> mockGuestList = Arrays.asList(
                new BasicGuestView(randomGuestCode1, "Family 1", false, "Hey there!"),
                new BasicGuestView(randomGuestCode2, "Family 2", false, "Hey there!")
        );

        Mockito.when(managerHandler.getGuestList()).thenReturn(mockGuestList);

        this.mockMvc.perform(get("/wedding-manager/guest-list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(mockGuestList.size()))
                .andExpect(jsonPath("$[0].code").value(randomGuestCode1.toString()))
                .andExpect(jsonPath("$[0].name").value("Family 1"))
                .andExpect(jsonPath("$[1].code").value(randomGuestCode2.toString()))
                .andExpect(jsonPath("$[1].name").value("Family 2"));
    }
}
