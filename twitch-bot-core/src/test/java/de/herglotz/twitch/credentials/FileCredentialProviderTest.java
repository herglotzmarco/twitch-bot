package de.herglotz.twitch.credentials;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class FileCredentialProviderTest {

	private File file;

	@Before
	public void setup() throws Exception {
		URL url = this.getClass().getClassLoader().getResource("test_credentials.properties");
		file = new File(url.toURI());
	}

	@Test(expected = InvalidPropertiesFileException.class)
	public void testNotExistingFile() throws Exception {
		new FileCredentialProvider(new File("ThisFileDoesNotExist"));
	}

	@Test
	public void testGetBotUsername() throws Exception {
		FileCredentialProvider provider = new FileCredentialProvider(file);
		assertEquals("myUsername", provider.getBotUsername());
	}

	@Test
	public void testGetOAuthToken() throws Exception {
		FileCredentialProvider provider = new FileCredentialProvider(file);
		assertEquals("someToken", provider.getOAuthToken());
	}

	@Test
	public void testGetApiClientId() throws Exception {
		FileCredentialProvider provider = new FileCredentialProvider(file);
		assertEquals("anyId", provider.getApiClientId());
	}

}
