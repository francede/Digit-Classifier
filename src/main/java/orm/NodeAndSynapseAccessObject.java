package orm;

import java.util.List;

import org.hibernate.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.*;

public class NodeAndSynapseAccessObject {
	private static SessionFactory factory= null;
	
	@SuppressWarnings({ })
	private static SessionFactory session;

	public static void main(String[] args) {		

		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

		try{
			factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

		}
		catch(Exception e){
			System.out.println("Error");
			StandardServiceRegistryBuilder.destroy( registry );
			e.printStackTrace();
		}
	}
		
	
	/*
	public void createAllNodes() {	
		Session session = factory.openSession();
		Transaction transaction = null;

		try{
			transaction = session.beginTransaction();
			for (int i = 0 ; i < layers.lenght ; i++) {
				for (int j = 0 ; j < nodes.lenght; j++) {
					   Node n = new Node("id, #, layer, bias");
			         session.saveOrUpdate(n);
			         }
				}
			transaction.commit();
			}
		catch(Exception e){
			if (transaction!=null) transaction.rollback();
			throw e;
			}
	finally{
		session.close();
		}
		}
		*/
	
	public void createAllSynapses() {
		Session session = factory.openSession();
		Transaction transaction = null;
		//thän metodi jolla luodaan kaikki synapsit
		
	}
	
	public void getAllBiasesfromDB() {
		Session session = factory.openSession();
		Transaction transaction = null;

		try{
			transaction = session.beginTransaction();
			List result = session.createQuery( "from Node" ).list();
			for ( Node n : (List<Node>) result ) {
				System.out.println(n.getBias());
			}transaction.commit();
			}catch(Exception e){
				if (transaction!=null) transaction.rollback();
				throw e;
				}
			finally{
				session.close();
				}
		}
		
		
	public void getAllWeightsfromBD() {	
		Session session = factory.openSession();
		Transaction transaction = null;

		try{
			transaction = session.beginTransaction();
			List result = session.createQuery( "from Synapse" ).list();
			for ( Synapse s : (List<Synapse>) result ) {
				System.out.println(s.getWeight());
				}transaction.commit();
			}catch(Exception e){
				if (transaction!=null) transaction.rollback();
				throw e;
				}
			finally{
				session.close();
				}
		
		}
	
	
	public void getBiasesFromDB() {
		Session session = factory.openSession();
		int x;
		Transaction transaction = null;
		try{
			transaction = session.beginTransaction();
			String sql = "select * from Node where Layer='x'";
			
			List result = session.createQuery(sql).list();
			for ( Node n : (List<Node>) result ) {
				System.out.println(n.getBias());
				}transaction.commit();
			}catch(Exception e){
				if (transaction!=null) transaction.rollback();
				throw e;
				}
			finally{
				session.close();
				}
		
	}
	
	
	public void getWeightsFromDB() {
		Session session = factory.openSession();
		int y;
		Transaction transaction = null;
		try{
			transaction = session.beginTransaction();
			String sql = "select * from Node where Layer='y'";
			
			List result = session.createQuery( "from Node" ).list();
			for ( Synapse s : (List<Synapse>) result ) {
				System.out.println(s.getWeight());
				}transaction.commit();
			}catch(Exception e){
				if (transaction!=null) transaction.rollback();
				throw e;
				}
			finally{
				session.close();
				}
		
	}
	
	}

