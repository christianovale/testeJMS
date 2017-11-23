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

public class TesteConsumidorDLQ {
	
	public static void main(String[] args) throws Exception {
		InitialContext context = new InitialContext();

		//importe do package javax.jms
		ConnectionFactory cf = (ConnectionFactory)context.lookup("ConnectionFactory");
		Connection conexao = cf.createConnection();
		conexao.start();

		//Session session = conexao.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		Session session = conexao.createSession(true, Session.SESSION_TRANSACTED);
		Destination fila = (Destination) context.lookup("DLQ");
		MessageConsumer consumer = session.createConsumer(fila);
		 
		//Consome uma mensagem
		//Message message = consumer.receive();
		//System.out.println("Recebendo a mensagem: "+ message);
		
		//Ouvindo mensagens o tempo todo
		consumer.setMessageListener(new MessageListener() {
			
			public void onMessage(Message msg) {
				System.out.println(msg);
				TextMessage textMessage = (TextMessage) msg;
				try {
					//textMessage.acknowledge();//Confirma o recebimwnto da mensagems CLIENT_ACKNOWLEDGE
					System.out.println(textMessage.getText());
					session.commit();//SESSION_TRANSACTED
				} catch (JMSException e) {
					try {
						session.rollback();//SESSION_TRANSACTED
					} catch (JMSException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
