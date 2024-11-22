package com.example.crud_javaa;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        int opcao;
        do {
            exibirMenu();
            String opcaoStr = reader.nextLine();
            
            if (!opcaoStr.isEmpty()) {
                try {
                    opcao = Integer.parseInt(opcaoStr);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Por favor, insira um número.");
                    continue;
                }

                switch (opcao) {
                    case 0 -> salvarAluno(reader);
                    case 1 -> buscarTodosAlunos();
                    case 2 -> buscarAlunoPorMatricula(reader);
                    case 3 -> atualizarAluno(reader);
                    case 4 -> excluirAluno(reader);
                    case 5 -> System.exit(0);
                    default -> System.out.println("Opção inválida!");
                }
            } else {
                System.out.println("Opção não pode ser vazia. Por favor, insira um número.");
            }
        } while (true);
    }

    private static void exibirMenu() {
        System.out.println("\n### Menu de Operações ###");
        System.out.println("0. Salvar novo Aluno");
        System.out.println("1. Buscar todos os Alunos");
        System.out.println("2. Buscar Aluno por Matrícula");
        System.out.println("3. Atualizar Aluno");
        System.out.println("4. Excluir Aluno");
        System.out.println("5. Sair do programa");
        System.out.print("Escolha uma opção: ");
    }

    public static void salvarAluno(Scanner reader) {
        System.out.println("\n### Criar Novo Aluno ###");

        System.out.print("Matrícula = ");
        var matricula = reader.nextInt();
        reader.nextLine();

        System.out.print("Nome = ");
        var nome = reader.nextLine();

        System.out.print("Email = ");
        var email = reader.nextLine();

        System.out.print("Curso = ");
        var curso = reader.nextLine();

        try (var conexao = Conexao.obterconexao()) {
            System.out.println("Banco conectado com sucesso");
            var sql = "INSERT INTO alunos (matricula, nome, email, curso) VALUES (" + matricula + ", '" + nome + "', '" + email + "', '" + curso + "')";
            
            try (Statement stmt = conexao.createStatement()) {
                stmt.executeUpdate(sql);
                System.out.println("Aluno cadastrado com sucesso.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void buscarTodosAlunos() {
        System.out.println("\n### Buscar Todos os Alunos ###");

        try (var conexao = Conexao.obterconexao()) {
            var sql = "SELECT * FROM alunos";
            try (Statement stmt = conexao.createStatement()) {
                var consulta = stmt.executeQuery(sql);
                while (consulta.next()) {
                    int matricula = consulta.getInt("matricula");
                    String nome = consulta.getString("nome");
                    String email = consulta.getString("email");
                    String curso = consulta.getString("curso");

                    System.out.println("Matrícula: " + matricula + ", Nome: " + nome + ", Email: " + email + ", Curso: " + curso);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void buscarAlunoPorMatricula(Scanner reader) {
        System.out.println("\n### Buscar Aluno por Matrícula ###");

        try (var conexao = Conexao.obterconexao()) {
            try (Statement stmt = conexao.createStatement()) {
                System.out.print("Insira a Matrícula que deseja buscar: ");
                int matricula = reader.nextInt();
                reader.nextLine();

                var sql = "SELECT matricula, nome, email, curso FROM alunos WHERE matricula = " + matricula;

                var consulta = stmt.executeQuery(sql);

                if (consulta.next()) {
                    String nome = consulta.getString("nome");
                    String email = consulta.getString("email");
                    String curso = consulta.getString("curso");

                    System.out.println("Matrícula: " + matricula + ", Nome: " + nome + ", Email: " + email + ", Curso: " + curso);
                } else {
                    System.out.println("Aluno não encontrado com a Matrícula: " + matricula);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void atualizarAluno(Scanner reader) {
        System.out.println("\n### Atualizar Aluno ###");

        try (var conexao = Conexao.obterconexao()) {
            try (Statement stmt = conexao.createStatement()) {
                System.out.print("Digite a Matrícula do Aluno que deseja alterar os dados: ");
                int matricula = reader.nextInt();
                reader.nextLine();

                var sql2 = "SELECT * FROM alunos WHERE matricula = " + matricula;
                var consultaid = stmt.executeQuery(sql2);

                if (consultaid.next()) {
                    String nome = consultaid.getString("nome");
                    String email = consultaid.getString("email");
                    String curso = consultaid.getString("curso");

                    System.out.println("Matrícula: " + matricula + ", Nome: " + nome + ", Email: " + email + ", Curso: " + curso);

                    System.out.print("Digite o novo nome: ");
                    var novoNome = reader.nextLine();

                    System.out.print("Digite o novo email: ");
                    var novoEmail = reader.nextLine();

                    System.out.print("Digite o novo curso: ");
                    var novoCurso = reader.nextLine();

                    var sqlUpdate = "UPDATE alunos SET nome = '" + novoNome + "', email = '" + novoEmail + "', curso = '" + novoCurso + "' WHERE matricula = " + matricula;
                    stmt.executeUpdate(sqlUpdate);
                    System.out.println("Aluno atualizado com sucesso.");
                } else {
                    System.out.println("Aluno não encontrado com a Matrícula: " + matricula);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void excluirAluno(Scanner reader) {
        System.out.println("\n### Excluir Aluno ###");

        try (var conexao = Conexao.obterconexao()) {
            try (Statement stmt = conexao.createStatement()) {
                System.out.print("Digite a Matrícula do Aluno que deseja excluir: ");
                int matricula = reader.nextInt();
                reader.nextLine();

                var sql = "DELETE FROM alunos WHERE matricula = " + matricula;
                stmt.executeUpdate(sql);
                System.out.println("Aluno com a Matrícula " + matricula + " foi removido.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
