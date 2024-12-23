package demo.usul.controller;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@WebAppConfiguration
class ReckonerControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private ServletContext servletContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        servletContext = webApplicationContext.getServletContext();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void givenWac_whenServletContext_thenItProvidesGreetController() {
        assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
        assertNotNull(webApplicationContext.getBean("reckonerController"));
    }

    void testRequestParam() {
//        mockMvc.perform(RequestBuilder)
    }

    @Test
    void givenGreetURI_whenMockMVC_thenVerifyResponse() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/v1/reckoner")
                        .param("id", "1")
                        .param("name", "John Doe"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

}