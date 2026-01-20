# TP 3 - Kafka Stream avec Avro

### Application Java Kafka Stream avec Avro

Créez un petit projet Java Maven pour interagir avec Kafka Stream et Avro.

1. **Créer un projet Maven** ou utiliser celui du TP précédent.
2. **Ajouter les dépendances pour Kafka Stream et Avro** dans `pom.xml` :
```xml
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-streams</artifactId>
    <version>4.1.1</version>
</dependency>
<dependency>
    <groupId>org.apache.avro</groupId>
    <artifactId>avro</artifactId>
    <version>1.12.1</version>
</dependency>

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.14.1</version>
    <configuration>
        <source>25</source>
        <target>25</target>
    </configuration>
</plugin>
```

3. **Créer une classe Avro** pour représenter un étudiant. Vous pouvez utiliser l'outil Avro https://mvnrepository.com/artifact/org.apache.avro/avro-maven-plugin
pour générer la classe Java à partir d'un schéma Avro.

Example de classe Avro:
```avsc
{
 "namespace": "example.avro",
 "type": "record",
 "name": "Movie",
 "fields": [
     {"name": "name", "type": "string"},
     {"name": "length",  "type": "double"}
 ]
}
```

4. **Créer un producer** qui envoie des objets Avro représentant des étudiants dans un topic Kafka 
nommé `students-avro`. Il faudra utiliser un l'encodeur Avro de la classe Student généré.

Attention: Le type de sérialisation dans le topic sera byte[]. Comme dans le TP précédent, 
vous pouvez créer un sérializer et un déserializer pour Avro.

5. **Créer une application Kafka Streams** qui lit les messages du topic `students-avro` et affiche 
les informations des étudiants dans la console.

- Créer un serde nommé SerdeAvro pour la classe Student.
- Configurer l'application Kafka Streams avec les propriétés nécessaires : BOOTSTRAP_SERVERS_CONFIG et APPLICATION_ID_CONFIG.
- Construire le flux de données pour lire depuis le topic `students-avro`, il faut pour ce faire :
  - Un StreamsConfig.
  - Un StreamsBuilder avec un KStream pour lire les messages du topic.
- Pour chaque message lu, afficher les informations de l'étudiant dans la console.

6. **Effectuer l'opérations suivantes** et mettre les résultats dans la console ainsi que dans un nouveau topic `students-processed` :
   - Filtrer les étudiants âgés de plus de 20 ans ainsi que ceux dont le diplôme d'ingénieur est "IT".

## Questions de synthèse

1. **Pourquoi utilise-t-on Avro plutôt que JSON ?**
Avro permet de generer des classes en compile time a partir d'un schéma, cela permet une grande flexibilité. De plus des outils intégrés a Avro permettent d'effectuer des opérations et de generer du code automatiquement.
2. **Qu'est-ce qu'un Serde et pourquoi en a-t-on besoin ?**
Un serde est une classe qui expose deux fonctions qui permettent de recuperer les serializer et deserializer d'une classe.
3. **Quelle est la différence entre `foreach()` et `to()` dans Kafka Streams ?**
foreach lit les résultats et applique une fonction.
to renvoie les données au topics spécifié.
4. **Comment Kafka Streams gère-t-il la tolérance aux pannes ?**
5. **Que se passe-t-il si on redémarre l'application Streams ? Les données sont-elles retraitées ?**
