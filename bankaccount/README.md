# BankAccount Application

## Description

BankAccount est une application de gestion de comptes bancaires. Elle permet de créer des comptes bancaires standards et des comptes bancaires limités avec des fonctionnalités de dépôt, retrait et gestion de découvert.

## Fonctionnalités

- **Création de comptes bancaires** : Créez des comptes bancaires standard et limités.
- **Dépôt** : Déposez de l'argent sur un compte.
- **Retrait** : Retirez de l'argent d'un compte.
- **Gestion du découvert** : Définissez et gérez les limites de découvert pour les comptes standard.
- **Relevé Bancaire** : Consultez toutes les transactions effectuées sur le compte sur un mois glissant

## Prérequis

- Java 22
- Maven
- Spring Boot

## Installation et Lancement

1. **Cloner le dépôt** :
    ```sh 
    cd bankaccount 
    ```

   
2. **Construire le projet avec Maven :**

   ```sh 
    mvn clean install 
    ```
3. Lancer BankAccountApplication.java
    ```sh 
    java -jar BankaccountApplication.java
    ```
## Accéder à la Console H2
Pour des questions de légèreté, d'économies de ressources et de simplicité, cette application utilise le système de base de données H2, développé en Java pour du Java, et offrant des fonctionnalités adaptées au développement et au test.

Assurez-vous que l'application est en cours d'exécution.

Ouvrez votre navigateur web et accédez à l'URL suivante :

http://localhost:8080/h2-console

Utilisez les informations de connexion suivantes pour accéder à la base de données :
```chatinput
    JDBC URL : jdbc:h2:mem:testdb
    User Name : sa
    Password : password
```


## Exemple de requêtes API
Créer un compte standard
````sh
POST /accounts/createAccount
Content-Type: application/json

{
"balance": 200.0,
"overdraftLimit": 50.0
}
````
Créer un compte limité
````sh
POST /limitedAccounts/createAccount
Content-Type: application/json

{
"balance": 200.0,
"depositLimit": 500.0
}
````
Consulter un compte
````sh
GET /accounts/{accountNumber}
````
Dépôt sur un compte
````sh
POST /accounts/{accountNumber}/deposit
Content-Type: application/x-www-form-urlencoded

amount=50.0
````
Retrait d'un compte
````sh
POST /accounts/{accountNumber}/withdraw
Content-Type: application/x-www-form-urlencoded

amount=30.0
````
Définir une limite de découvert (lève une exception si le compte est un compte limité)
````sh
POST /accounts/{accountNumber}/setOverDraftLimit
Content-Type: application/x-www-form-urlencoded

amount=100.0
````
Obtenir un relevé de compte
````sh
GET /accounts/{accountNumber}/statement
````

## Créer un container de l'application

`````shell
mvn package
docker build -t bankaccount
docker run -p 8080:8080 bankaccount
`````