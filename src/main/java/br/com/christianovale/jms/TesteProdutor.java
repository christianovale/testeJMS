package br.com.christianovale.jms;

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
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class TesteProdutor {
	
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
		
		MessageProducer producer = session.createProducer(fila);
		
		for(int i=0 ; i<=100 ; i++){
			
			Message message = session.createTextMessage("<pedido><id>" + i + "</id></pedido>");
			producer.send(message);
		}
		
		//new Scanner(System.in).nextLine();
	
		session.close();
		conexao.close();    
		context.close();
	}


}
