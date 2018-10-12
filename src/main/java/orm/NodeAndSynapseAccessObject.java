package orm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.hibernate.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.*;

public class NodeAndSynapseAccessObject {
	private static SessionFactory factory = null;

	public NodeAndSynapseAccessObject() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		try {
			factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			System.out.println("Error");
			StandardServiceRegistryBuilder.destroy(registry);
			e.printStackTrace();
		}
	}

	public void createAllNodes(ArrayList<double[]> biasesOfLayers) {
		System.out.println("Inserting nodes to database");
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			for (int i = 0; i < biasesOfLayers.size(); i++) {
				double[] biasesOfLayer = biasesOfLayers.get(i);
				for (int j = 0; j < biasesOfLayer.length; j++) {
					if (j % 1000 == 0 & j != 0) {
						System.out.println("Inserting node no. " + j + "/" + biasesOfLayer.length + " of layer " + i + "/" + biasesOfLayers.size());
					}
					Node n = new Node(0, j, i, biasesOfLayer[j]);
					session.saveOrUpdate(n);
				}
			}
			transaction.commit();
			System.out.println("Done inserting nodes to database");
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	public void createAllSynapses(ArrayList<double[]> weightsOfLayers) {
		System.out.println("Inserting synapses to database");
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			for (int i = 0; i < weightsOfLayers.size(); i++) {
				double[] weightsOfLayer = weightsOfLayers.get(i);
				for (int j = 0; j < weightsOfLayer.length; j++) {
					if (j % 1000 == 0 & j != 0) {
						System.out.println("Inserting synapse no. " + j + "/" + weightsOfLayer.length + " of synapse layer " + i + "/" + weightsOfLayers.size());
					}
					Synapse s = new Synapse(0, i, weightsOfLayer[j]);
					session.saveOrUpdate(s);
				}
			}
			transaction.commit();
			System.out.println("Done inserting synapses to database");
			
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	public ArrayList<double[]> getAllBiasesfromDB() {
		System.out.println("Reading biases from database");
		List<Double> biasList = new ArrayList<Double>();
		ArrayList<double[]> biasArrayList = new ArrayList<double[]>();
		Session session = factory.openSession();
		Transaction transaction = null;
		int layer = 0;
		try {
			List result = null;
			do {
				String sql = "from Node where Layer='" + layer + "'";
				result = session.createQuery(sql).list();
				transaction = session.beginTransaction();
				Double[] arr = new Double[result.size()];
				for (Node n : (List<Node>) result) {
					biasList.add(n.getBias());
				}
				arr = biasList.toArray(arr);
				if (!result.isEmpty()) {
					biasArrayList.add(Stream.of(arr).mapToDouble(Double::doubleValue).toArray());
				}
				transaction.commit();
				layer++;
				biasList = new ArrayList<Double>();
			} while (!result.isEmpty());
			System.out.println("Loading biases done");

		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw e;
		} finally {
			session.close();
		}
		return biasArrayList;
	}

	public ArrayList<double[]> getAllWeightsfromBD() {
		System.out.println("Reading weights from database");
		List<Double> weightList = new ArrayList<Double>();
		ArrayList<double[]> weightArrayList = new ArrayList<double[]>();
		Session session = factory.openSession();
		Transaction transaction = null;
		int layer = 0;
		try {
			List result = null;
			do {
				String sql = "from Synapse where Layer='" + layer + "'";
				result = session.createQuery(sql).list();
				transaction = session.beginTransaction();
				Double[] arr = new Double[result.size()];
				for (Synapse s : (List<Synapse>) result) {
					weightList.add(s.getWeight());
				}
				arr = weightList.toArray(arr);
				if (!result.isEmpty()) {
					weightArrayList.add(Stream.of(arr).mapToDouble(Double::doubleValue).toArray());
				}

				transaction.commit();
				layer++;
				weightList = new ArrayList<Double>();
			} while (!result.isEmpty());
			System.out.println("Loading weights done");

		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw e;
		} finally {
			session.close();
		}
		return weightArrayList;
	}

	public void deleteAllDataInDatabase() {
		System.out.println("Deleting all data in database");
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.createQuery("delete from Node").executeUpdate();
			transaction.commit();
			transaction = session.beginTransaction();
			session.createQuery("delete from Synapse").executeUpdate();
			transaction.commit();
			System.out.println("Deleting all data in database done");
		} catch (Exception e) {
			System.out.println(e);
			if (transaction != null)
				transaction.rollback();
		} finally {
			session.close();
		}

	}
}
