package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicationController.class)
public class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @Test
    @WithMockUser(roles = "USER") // Only if your endpoint is secured
    public void testGetAllApplications() throws Exception {
        Application application = new Application();
        application.setId(1L);
        application.setJobId("job123");
        application.setUserId("user456");
        application.setCoverLetter("I am very interested in this role.");

        Mockito.when(applicationService.getAllApplications())
                .thenReturn(Collections.singletonList(application));

        mockMvc.perform(get("/api/applications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].jobId").value("job123"))
                .andExpect(jsonPath("$[0].userId").value("user456"))
                .andExpect(jsonPath("$[0].coverLetter").value("I am very interested in this role."));
    }
}