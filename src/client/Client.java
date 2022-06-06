package client;

import gui.autent.Autentification;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Properties;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.JOptionPane;

import vfs.ReadConfig;

public class Client{
	
	private static final String CLIENT_CONFIG = "configs/client_config.ini";
	private static final String IP_KEY = "ip";
	private static final String PORT_KEY = "port";
	private static final String KEY_STORE = "key_store";
	private static final String PASSPHRASE = "passphrase";
	private static final String KEY_STORE_PASSWORD = "key_store_password";
	
	private static SSLSocket socket;
	
	private BufferedReader reader;
	private PrintWriter writer;
	
	
	public Client()
	{
		Properties property = new ReadConfig().readConfig(CLIENT_CONFIG);
		
		try
		{
			// Получить экземпляр хранилища ключей.
            KeyStore keyStore = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(property.getProperty(KEY_STORE));
            keyStore.load(fis, property.getProperty(PASSPHRASE).toCharArray());
            
            // Получить диспетчеры ключей базовой реализации для заданного хранилища ключей.
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, property.getProperty(KEY_STORE_PASSWORD).toCharArray());
            KeyManager [] keyManagers = keyManagerFactory.getKeyManagers();
            
            // Получить доверенные диспетчеры базовой реализации.
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);
            TrustManager [] trustManagers = trustManagerFactory.getTrustManagers();
            
            // Получить защищенное случайное число.
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
            
            // Создание SSL контекста
            SSLContext sslContext = SSLContext.getInstance("SSLv3");
            sslContext.init(keyManagers, trustManagers, secureRandom);
            
            // Создание фабрики SSL сокетов.
            javax.net.ssl.SSLSocketFactory sslSocketFactory =
                    sslContext.getSocketFactory();
			socket = (SSLSocket) sslSocketFactory.createSocket(property.getProperty(IP_KEY), Integer.parseInt(property.getProperty(PORT_KEY)));
			try
			{
				this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.writer = new PrintWriter(socket.getOutputStream(), true);
				
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			
			
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Отсутствует соединение!\nВозможно не настроена конфигурация на сервере.", "Ошибка", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	
	public void startClient()
	{
		new Thread(new Autentification(this.reader, this.writer)).start();
	}

	public static SSLSocket getSocket()
	{
		return socket;
	}
	
}
