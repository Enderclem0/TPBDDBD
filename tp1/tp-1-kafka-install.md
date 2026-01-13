# TP 1 - Kafka Installation et commande de base

**Objectif** : Découvrir Apache Kafka, installer l'environnement et manipuler les concepts de base (topics, producers, consumers).

**Prérequis** : 
- Java JDK 17+ installé
- Docker et Docker Compose installés

---

## Partie 1 - Installation du JAR

### 1.1 Téléchargement de Kafka

1. Rendez-vous sur le site officiel : https://kafka.apache.org/downloads
2. Téléchargez la dernière version stable
3. Décompressez l'archive dans un répertoire de travail

### 1.2 Démarrage de l'environnement Kafka

Kafka nécessite un broker, par défaut ZooKeeper est recommandé.
Néanmoins, depuis les dernières version et pour travailler en local, il existe une version KRaft. Pour cette partie, nous utiliserons cette version

1. **Générer un ID de cluster** :
```bash
   KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"
```

2. **Formater le répertoire de logs** :
```bash
   bin/kafka-storage.sh format --standalone -t $KAFKA_CLUSTER_ID -c config/server.properties
```

3. **Démarrer le serveur Kafka** :
```bash
   bin/kafka-server-start.sh config/server.properties
```

4. Vérifiez que Kafka est démarré (le terminal doit afficher des logs sans erreurs)

**Laissez ce terminal ouvert** - Kafka tourne en foreground

### 1.3 Manipulation des Topics

Ouvrez un **nouveau terminal** dans le même répertoire Kafka.

1. **Créer un topic** nommé `test-topic` avec 3 partitions en utilisant la commande bin/kafka-topics.sh
bin/kafka-topics.sh --create --topic test-topic --partitions 3 --bootstrap-server localhost:9092
Created topic test-topic.

2. **Lister les topics**

3. **Décrire le topic** créé précédemment `test-topic`

### 1.4 Producteur et Consommateur en ligne de commande

Ouvrez un **nouveau terminal** dans le même répertoire Kafka.

1. **Démarrer un producteur** sur le topic `test-topic`

bin/kafka-console-producer.sh --topic test-topic --bootstrap-server localhost:9092
   
Tapez quelques messages. Chaque ligne correspond à un message.

Ouvrez un **nouveau terminal** dans le même répertoire Kafka.

2. **Démarrer un consommateur** sur le topic `test-topic`
bin/kafka-console-consumer.sh --topic test-topic --bootstrap-server localhost:9092
- Pourquoi les messages envoyez précédemment ne sont-ils pas reçu ?
Car il ne recoie les messages qu'après être lancé
3. **Expérimentez** :
   - Envoyez de nouveaux messages depuis le producteur
   - Observez-les apparaître dans le consommateur
   - Arrêtez le consommateur (Ctrl+C) et démarrez-le **avec** `--from-beginning`. Que se passe-t-il ?
Le consommateur recoit tout les messages rétroactivement

4. Rajoutez un **consumer group** lors du lancement du topic `avec --group group`
bin/kafka-console-consumer.sh --topic test-topic --bootstrap-server localhost:9092 --group mon-groupe-tp

5. **Questions à répondre** :
    - Lancez ce consommateur 2 fois en parallèle (2 terminaux différents avec même groupe). Envoyez des messages. Comment sont-ils distribués ?
Un des deux recoit tous les messages

### 1.5 Arrêt de l'installation JAR

Arrêtez tous les processus Kafka lancés précédemment.

---

## Partie 2 - Utilisation avec Docker

Docker simplifie grandement le déploiement de Kafka. Nous allons recréer notre environnement avec Docker Compose.

### 2.1 Configuration Docker Compose

1. **Créer un fichier `docker-compose-zookeeper.yml` en comprenant l'image `confluentinc/cp-kafka:7.4.4` avec un ZooKeeper `confluentinc/cp-zookeeper:7.4.4`**

2. **Créer un fichier `docker-compose-kraft.yml` en comprenant l'image `confluentinc/cp-kafka:8.0.3` mode KRaft**

Note: Pour rajouter un visualiseur :
```yaml
services:
  kafka-ui:
    image: kafbat/kafka-ui:latest
    container_name: kafka-ui
    depends_on:
      - kafka-broker
    ports:
      - "9091:8080"
    environment:
      DYNAMIC_CONFIG_ENABLED: true
      KAFKA_CLUSTERS_0_NAME: kafka-broker
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka-broker:29092
    networks:
      - network
    restart: unless-stopped
```

3. **Démarrer Kafka** :
```bash
   docker compose -f docker-compose-kraft up -d
```

4. **Vérifier les logs** :
```bash
   docker compose logs -f kafka-broker
```

Attendez que Kafka soit complètement démarré. Un message devrait appararaitre dans les logs :
```
    INFO [KafkaRaftServer nodeId=1] Kafka Server started (kafka.server.KafkaRaftServer)
```

### 2.2 Manipulation des Topics avec Docker

1. **Connexion au docker** :
```bash
    docker exec -it broker sh
```

2. **Créer un topic `etudiants`**
kafka-topics --create --topic etudiants --bootstrap-server localhost:9092

3. **Lister les topics et décrire le topic `etudiants`**
kafka-topics --list --bootstrap-server localhost:9092

4. **Créer un producteur Docker sur le topic `etudiants`**
kafka-console-producer --topic etudiants --bootstrap-server localhost:9092

Envoyez quelques messages représentant des étudiants.
(Attention : Une ligne correspond à un message)
```json
{
    "firstName": "test",
    "lastName": "test",
    "age": 21,
    "engineeringDegree": "IT"
}
```

5. **Créer un consumateur Docker sur le topic `etudiants`**
kafka-console-consumer --topic etudiants --bootstrap-server localhost:9092 --from-beginninging

### 2.3 Nettoyage
```bash
docker compose down -v
```

---

## Questions de synthèse

1. Quelle est la différence entre une partition et un topic ?
Un topic est la représentation logique d'un "dossier de message".
Les partitions sont les subdivision physique d'un topic.
Lors de l'envoi d'un message le broker choisi un topic en fonction de la clé ou du hash d'un message.
Une partition peut etre lue par un seul consommateur.
2. À quoi sert un consumer group ?
Il permet de regrouper plusieurs instances de consommateurs. Kafka distribue les partitions du topic entre les membres du groupe. Cela permet de paralléliser le traitement et assure que chaque message n'est traité que par un seul consommateur du groupe.
3. Quels sont les avantages de Docker pour le développement avec Kafka ?
Portabilité, simplicité d'installation, reproductibilité, nettoyage facile.
4. Que se passe-t-il si on a plus de consommateurs que de partitions dans un groupe ?
Les consommateurs en trop seront inactifs. Kafka ne peut pas assigner une partition à plus d'un consommateur à la fois dans un même groupe.
5. Comment Kafka garantit-il l'ordre des messages ?
Kafka garantit l'ordre uniquement au sein d'une partition, mais pas sur tout le topic. Si l'ordre total est nécessaire, il faut utiliser une seule partition (perte de performance).
