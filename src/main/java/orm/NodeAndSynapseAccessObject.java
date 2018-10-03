package orm;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.*;
import org.hibernate.cfg.Configuration;

public class NodeAndSynapseAccessObject {
	private static SessionFactory factory= null;
	
	@SuppressWarnings({ })
	private static SessionFactory session;
<<<<<<< HEAD

	public static void main(String[] args) {		

=======
	
	public NodeAndSynapseAccessObject()  {
>>>>>>> 1efd1d458228afbd5d6cea7b044d99645e938cac
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
	
	public void createAllNodes(ArrayList<double[]> weightsOfLayers) {	
		Session session = factory.openSession();
		Transaction transaction = null;

		try{
			transaction = session.beginTransaction();
			for (int i = 0 ; i < weightsOfLayers.size(); i++) {
				double[] weightsOfLayer = weightsOfLayers.get(i);
				for (int j = 0 ; j < weightsOfLayer.length; j++) {
					   Node n = new Node(0, j, i, weightsOfLayer[j]);
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

