# MusicShop

## Requirements

Za pokretanje aplikacije potrebno je imati instalirano sljedeće:

- **Java 21**  
  Provjera instalacije:
    ```
  java -version
  ```

- **Maven 3+**  
Provjera instalacije:
  ```
  mvn -version
  ```

- **Docker**  
Provjera instalacije:

  ```
  docker --version
  ```

---

## Pokretanje aplikacije

### 1. Pokretanje aplikacije za razvoj (dev način)

1. Pokreni potrebne servise s Docker Compose:
  ```
  docker compose up -d
  ```

2. Izgradi projekt:
  ```
  mvn clean install
  ```

3. Pokreni aplikaciju:
  ```
  mvn spring-boot:run
  ```

---

### 2. Pokretanje aplikacije preko JAR datoteke

1. Pokreni potrebne servise s Docker Compose:
  ```
  docker compose up -d
  ```

2. Izgradi JAR datoteku:
  ```
  mvn clean package
  ```

3. Pokreni aplikaciju:
  ```
  java -jar target/musicshop-0.0.1-SNAPSHOT.jar
  ```

---

> **Napomena:**  
> Prije svakog pokretanja, provjeri da su Docker i svi servisi iz `docker-compose.yml` ispravno pokrenuti.
> Aplikacija je dostupna na adresi: [http://localhost:8080](http://localhost:8080)
