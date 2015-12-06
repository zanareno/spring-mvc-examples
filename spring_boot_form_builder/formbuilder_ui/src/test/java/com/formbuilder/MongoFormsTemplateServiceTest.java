package com.formbuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import lombok.val;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class MongoFormsTemplateServiceTest {

	@Autowired
	FormInformationService formsService;

	@Before
	public void init() throws IOException {
		assertNotNull(formsService);
		formsService.deleteAll();
		val payload = readFile("src/test/resources/schema/form-template.json", Charset.defaultCharset());
		val om = new ObjectMapper();
		val formTemplate = om.readValue(payload, JSONObject.class);
		formTemplate.put("entryType", "Form");
		formsService.save(formTemplate, "vendor_management", "fuelload", 0);
	}
	
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<Map> list = formsService.findAllFormTemplates("vendor_management");
		
		assertNotNull(list);
		assertEquals(list.size(), 1);

		assertEquals(list.get(0).get("group"), "Form");
		assertNotNull(list.get(0).get("tableList"));
	}
	
	@Test
	public void testFindByName() throws JsonParseException, JsonMappingException, IOException {
		val formTemplate= formsService.getData("vendor_management", "fuelload", "0");
				
		assertNotNull(formTemplate);
	}
}