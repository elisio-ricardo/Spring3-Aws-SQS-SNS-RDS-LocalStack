package br.com.elisio.spring3awsLocalStack.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"idAluno", "nome", "cpf", "rg", "data_nascimento", "endereco", "cep", "cidade", "estado", "curso"})
public class AlunoResponse extends AlunoRequest {

}