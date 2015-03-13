package edu.capella.ime.config;

import javax.servlet.ServletContext;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;
import org.springframework.web.context.ServletContextAware;

@Configuration
@EnableHypermediaSupport(type = { HypermediaType.HAL })
@AutoConfigureAfter(WebConfigurer.class)
public class HATEOASConfiguration implements ServletContextAware {

	public static String CURIE_NAMESPACE = "ime";
	
	private ServletContext servletContext;
	
	@Bean
	public CurieProvider curieProvider() {
		
		String contextPath = servletContext.getContextPath();
		
		return new DefaultCurieProvider(CURIE_NAMESPACE, new UriTemplate(
				contextPath + "/rels/{rel}"));
	}

	@Override
	public void setServletContext(ServletContext servletContext) {

		this.servletContext = servletContext;
	}

}
