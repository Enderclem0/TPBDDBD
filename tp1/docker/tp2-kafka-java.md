# TP 2 - Kafka avec Java

### Application Java Producer/Consumer

Créez un petit projet Java Maven pour interagir avec Kafka.

1. **Créer un projet Maven**

2. **Ajouter les dépendances pour Kafka** dans `pom.xml` https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients

<br>

**Pour les deux questions suivantes, vous pouvez écrire un message basique comme un String dans le topic**

3. **Créer dans une classe un producer qui écrit dans le topic "etudiants" crée au TP précédent**

Lancer la classe avec un fonction main et vérifier via sh ou via kafka-ui qu'un message existe dans le topic etudiants

4. **Créer dans une classe un consumer qui lit dans le topic "etudiants" crée au TP précédent**

Lancer la classe avec un fonction main et vérifier dans les logs que le message crée précédemment est bien consommé.
Pourquoi n'apparait-il pas ?

Comment faire pour relire le message à chaque fois ?

5. **Crée un nouveau producer et consommateur** pour envoyer des objets java byte à byte représentant des étudiants :
```json
   {
        "firstName": "Jean",
        "lastName": "Dupont",
        "age": 21,
        "engineeringDegree": "IT"
    }
```
Note : Pour sérializer bytes à bytes, il faudra créer un sérializer et un déserializer.
```java
Deserializer<?> et Serializer<?>
```

6. **Crée un nouveau producer et consommateur** pour envoyer des objets JSON représentant des étudiants

Note : Rajouter les dépendances pour pouvoir utiliser la transformation Json de la librairie Jackson
```xml
<dependency>
    <groupId>tools.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>3.0.3</version>
</dependency>
```

7. **Créer un second consommateur** dans le même groupe. 
Observez comment les partitions sont réparties.
Au début les messages sont réparti dans les deux puis au bout d'un moment un seul consommateur est solicité.
8. **Expérimentez avec les partitions** : 
- Créez un topic avec 1 partition, puis un autre avec 5 partitions. 
- Lancez plusieurs consommateurs et observez la différence.
1 partitions 2 consommateurs, un seul consommateur est solicité.
5 partitions 5 consommateurs, une seule partition (consommateur) est solicité au début puis les autres sont sollicités au relancement pour balancer la charge.
