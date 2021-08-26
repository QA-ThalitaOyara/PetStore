// 1 - Pacote
package petStore;

// 2 - Bibliotecas

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;


// 3 - Classe
public class Pet {
  // 3.1 - Atributos
  String uri = "https://petstore.swagger.io/v2/pet"; // endereço da entidade Pet

  // 3.2 - Métodos e Funções
  public String lerJson(String caminhoJson) throws IOException {
    return new String(Files.readAllBytes(Paths.get(caminhoJson)));
  }

  // Incluir - Create - Post
  @Test(priority = 1)  // Identifica o método ou função como um teste para o TestNG
  public void incluirPet() throws IOException {
    String jsonBody = lerJson("db/pet01.json");

    given() // Dado
            .contentType("application/json") // comum em API REST - antigas era "text/xml"
            .log().all()
            .body(jsonBody)
            .when()  // Quando
            .post(uri)
            .then()  // Então
            .log().all()
            .statusCode(200)
            .body("name", is("Channels"))
            .body("status", is("available"))
            .body("category.name", is("SDAD546054654A")) //quando voi checar um elemento que está entre pareteses, posso usar o IS, se estiver entre couchetes, tenho que usar o "contains", como na verificação a baixo.
            .body("tags.name", contains("sta - Semana do Teste de Api"))
    ;
  }
    @Test (priority = 2)
    public void consultarPet() throws IOException {

      String petId = "15203568";
      String token =
      given() // Dado
              .contentType("application/json") // comum em API REST - antigas era "text/xml"
              .log().all()

              .when()  // Quando
                .get(uri + "/" + petId)

              .then()  // Então
                .log().all()
               .statusCode(200)
               .body("name", is("Channels"))
               .body("status", is("available"))
               .body("category.name", is ("SDAD546054654A")) //quando voi checar um elemento que está entre pareteses, posso usar o IS, se estiver entre couchetes, tenho que usar o "contains", como na verificação a baixo.
               .body("tags.name", contains("sta - Semana do Teste de Api"))
              .extract()
              .path("category.name")
      ;

      System.out.println("O Token é " + token);
  }

  @Test(priority = 3)
  public void alterarPet () throws IOException {
    String jsonBody = lerJson("db/pet02.json");

    given() // Dado
            .contentType("application/json") // comum em API REST - antigas era "text/xml"
            .log().all()
            .body(jsonBody)
            .when()  // Quando
              .put(uri)
            .then()  // Então
              .log().all()
              .statusCode(200)
              .body("name", is("Channels"))
              .body("status", is("SOLD"))
              .body("category.name", is("SDAD546054654A")) //quando voi checar um elemento que está entre pareteses, posso usar o IS, se estiver entre couchetes, tenho que usar o "contains", como na verificação a baixo.
              .body("tags.name", contains("sta - Semana do Teste de Api"))
    ;
  }

  @Test (priority = 4)
  public void excluirPet () throws IOException {
    String petId = "15203568";
    given()
            .contentType("application/json")
            .log().all()
            .when()
              .delete(uri + "/" + petId)
            .then()
              .log().all()
              .statusCode(200)
            .body("code", is (200))
            .body("type", is ("unknown"))
            .body("message", is (petId))
    ;
    System.out.println("Exclusão do Pet " + petId + " realizada com sucesso");
  }
}