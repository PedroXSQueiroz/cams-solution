package br.com.pedroxsqueiroz.camsresourceserver;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.security.auth.kerberos.KeyTab;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.pedroxsqueiroz.camsresourceserver.config.NodeConfig;
import br.com.pedroxsqueiroz.camsresourceserver.models.ServerModel;
import br.com.pedroxsqueiroz.camsresourceserver.services.MessagingService;
import lombok.extern.java.Log;

@SpringBootApplication
@Log
public class CamsResourceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamsResourceServerApplication.class, args);
	}
	
	@Value("${node.key_location}") 
	private String localKeyLocation;
	
	@Autowired 
	private NodeConfig localNodeConfig;
	
	@Autowired
	private MessagingService messagingService;
	
	@Value("${node.name}")
	private String nodeName;
	
	@PostConstruct
	public void initNode() throws IOException, TimeoutException 
	{
		
		this.log.info(String.format("Starting node: %s.", this.nodeName));
		
		this.loadLocalKey();
		
		this.log.info( this.localNodeConfig.toString() );
		
		this.messagingService.connectServer( new ServerModel( this.localNodeConfig ) ); 
		
		this.messagingService.connectServers();
		
	}
	
	public void loadLocalKey( ) throws IOException 
	{
		
		Path localKeyPath = Paths.get(this.localKeyLocation);
		
		byte[] keyBytes = Files.exists(localKeyPath) ? 
									this.readLocalKey(localKeyPath) :
									this.createLocalKey(localKeyPath);
		
		String key = new String(keyBytes, Charset.forName("UTF-8"));
		
		this.localNodeConfig.setKey(key);
		
		
	}

	private byte[] readLocalKey(Path localKeyPath) throws IOException
	{		
		return Files.readAllBytes(localKeyPath);
	}
	
	private byte[] createLocalKey(Path localKeyPath) throws IOException {
		
		byte[] keyBuffer = new byte[25];
		new Random().nextBytes(keyBuffer);
		
		byte[] keyBase64 = Base64.encodeBase64(keyBuffer);
		
		Files.createFile(localKeyPath);
		Files.write(localKeyPath, keyBase64);
		
		return keyBase64;
		
	}
	
}
