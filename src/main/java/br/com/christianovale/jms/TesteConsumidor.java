package br.com.christianovale.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class TesteConsumidor {
	
	public static void main(String[] args) throws Exception {
		InitialContext context = new InitialContext();

		//importe do package javax.jms
		ConnectionFactory cf = (ConnectionFactory)context.lookup("ConnectionFactory");
		Connection conexao = cf.createConnection();
		conexao.start();

		Session session = conexao.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination fila = (Destination) context.lookup("financeiro");
		MessageConsumer consumer = session.createConsumer(fila);
		
		//Consome uma mensagem
		//Message message = consumer.receive();
		//System.out.println("Recebendo a mensagem: "+ message);
		
		//Ouvindo mensagens o tempo todo
		consumer.setMessageListener(new MessageListener() {
			
			public void onMessage(Message msg) {
				
				TextMessage textMessage = (TextMessage) msg;
				try {
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});

		
		new Scanner(System.in).nextLine();
	
		session.close();
		conexao.close();    
		context.close();
	}


}
