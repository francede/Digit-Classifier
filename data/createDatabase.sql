DROP DATABASE IF EXISTS neumeroverkko;
CREATE DATABASE neumeroverkko;
USE neumeroverkko;


CREATE TABLE Node
(
  Bias DOUBLE NOT NULL,
  NodeOrdinal INT NOT NULL,
  Layer INT NOT NULL,
  NodeID INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (NodeID)
);

CREATE TABLE Synapse
(
  Weight DOUBLE NOT NULL,
  SynapseId INT NOT NULL AUTO_INCREMENT,
  Layer INT NOT NULL,
  PRIMARY KEY (SynapseId)
);

CREATE TABLE NodeSynape
(
  NodeSynapseId INT NOT NULL,
  Direction VARCHAR(10) NOT NULL,
  NodeID INT NOT NULL,
  SynapseId INT NOT NULL,
  PRIMARY KEY (NodeSynapseId),
  FOREIGN KEY (NodeID) REFERENCES Node(NodeID),
  FOREIGN KEY (SynapseId) REFERENCES Synapse(SynapseId)
);