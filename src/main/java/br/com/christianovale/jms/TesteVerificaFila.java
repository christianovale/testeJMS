package br.com.christianovale.jms;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class TesteVerificaFila {
	/*
	 * 
	 * Dependendo da nossa aplicação, podemos precisar apenas checar (monitoramento) as mensagens que chegaram 
	 * para uma determinada fila sem consumi-la. Ou seja, apenas queremos ver sem tirá-las da fila. Para isso 
	 * podemos usar um componente do JMS chamado QueueBrowser, usado para navegar sobre as mensagens sem consumi-las.
	 */
	public static void main(String[] args) throws Exception {
		
		//Alternativa ao .jndi
		Properties properties = new Properties();
		properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");        
		properties.setProperty("java.naming.provider.url", "tcp://localhost:61616");
		properties.setProperty("queue.financeiro", "fila.financeiro");
		InitialContext context = new InitialContext(properties);
		
		//InitialContext context = new InitialContext();

		//importe do package javax.jms
		ConnectionFactory cf = (ConnectionFactory)context.lookup("ConnectionFactory");
		Connection conexao = cf.createConnection();
		conexao.start();

		Session session = conexao.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination fila = (Destination) context.lookup("financeiro");
		
		QueueBrowser browser = session.createBrowser((Queue) fila);
		
		Enumeration msgs = browser.getEnumeration();
		while (msgs.hasMoreElements()) { 
		    TextMessage msg = (TextMessage) msgs.nextElement(); 
		    System.out.println("Message: " + msg.getText()); 
		}
	
		session.close();
		conexao.close();    
		context.close();
	}


}
