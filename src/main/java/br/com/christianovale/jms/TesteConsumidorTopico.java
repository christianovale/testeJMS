package br.com.christianovale.jms;

import java.io.StringWriter;
import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.xml.bind.JAXB;

import br.com.caelum.modelo.Pedido;
import br.com.caelum.modelo.PedidoFactory;

public class TesteConsumidorTopico {
	
	public static void main(String[] args) throws Exception {
		InitialContext context = new InitialContext();

		//importe do package javax.jms
		ConnectionFactory cf = (ConnectionFactory)context.lookup("ConnectionFactory");
		Connection conexao = cf.createConnection();
		conexao.setClientID("comercial");
		conexao.start();

		Session session = conexao.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topico = (Topic) context.lookup("loja");
		MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");
		
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
		
		//Msnsagem via Objeto-----------------
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","java.lang,br.com.caelum.modelo");
	    
		Pedido pedido = new PedidoFactory().geraPedidoComValores();

		StringWriter writer = new StringWriter();
		JAXB.marshal(pedido, writer);
		String xml = writer.toString();

		Message message = session.createTextMessage(xml);
		//--------------------------------------
		
		new Scanner(System.in).nextLine();
	
		session.close();
		conexao.close();    
		context.close();
	}


}
