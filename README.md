# 💻 Projeto de Arquitetura D3

Projeto da disciplina de **Organização e Arquitetura de Computadores**
Este repositório contém a implementação da **Arquitetura D3**, parte do projeto da disciplina de Organização e Arquitetura de Computadores. A arquitetura foi construída em **Java**, utilizando a **IDE Eclipse**, e simula uma estrutura computacional capaz de executar instruções em um formato específico de assembly, conforme detalhado abaixo.

---

## 📚 Sobre o Projeto

O projeto tem como objetivo projetar e implementar diferentes arquiteturas de computadores, considerando os aspectos de sua organização interna. Nossa equipe desenvolveu a **Arquitetura D3**, que possui uma ULA (Unidade Lógica e Aritmética), registradores, pilha, memória e um conjunto de instruções definidas.

### 📌 Instruções Assembly Suportadas

| Comando                      | Descrição                              |
| ---------------------------- | -------------------------------------- |
| `add %<regA> %<regB>`        | RegB ← RegA + RegB                     |
| `add <mem> %<regA>`          | RegA ← memória[mem] + RegA             |
| `add %<regA> <mem>`          | Memória[mem] ← RegA + memória[mem]     |
| `add imm %<regA>`            | RegA ← imm + RegA                      |
| `sub <regA> <regB>`          | RegB ← RegA - RegB                     |
| `sub <mem> %<regA>`          | RegA ← memória[mem] - RegA             |
| `sub %<regA> <mem>`          | memória[mem] ← RegA - memória[mem]     |
| `sub imm %<regA>`            | RegA ← imm - RegA                      |
| `move <mem> %<regA>`         | RegA ← memória[mem]                    |
| `move %<regA> <mem>`         | memória[mem] ← RegA                    |
| `move %<regA> %<regB>`       | RegB ← RegA                            |
| `move imm %<regA>`           | RegA ← immediate                       |
| `inc %<regA>`                | RegA++                                 |
| `jmp <mem>`                  | PC ← mem                               |
| `jn <mem>`                   | Se última operação < 0, então PC ← mem |
| `jz <mem>`                   | Se última operação = 0, então PC ← mem |
| `jeq %<regA> %<regB> <mem>`  | Se RegA == RegB, então PC ← mem        |
| `jneq %<regA> %<regB> <mem>` | Se RegA != RegB, então PC ← mem        |
| `jgt %<regA> %<regB> <mem>`  | Se RegA > RegB, então PC ← mem         |
| `jlw %<regA> %<regB> <mem>`  | Se RegA < RegB, então PC ← mem         |
| `call <mem>`                 | Empilha PC e desvia para mem           |
| `ret`                        | PC ← pop()                             |

---

## 🧠 Diagrama da Arquitetura

<p align="center"> <img width="527" height="382" alt="image" src="https://github.com/user-attachments/assets/1f4570b9-4c39-4cfa-a428-0587817615a2" /> <br /> <em>Imagem representando a organização da Arquitetura D3 utilizada no projeto.</em> </p>

---

## 🧪 Como executar

### ✅ Pré-requisitos

- Java 7
- Eclipse IDE (recomendado Eclipse IDE for Java Developers)

### 📥 Clonando o Projeto

1. Clone este repositório:

```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
```

2. Abra o Eclipse.
3. Vá em **File > Import > Existing Projects into Workspace**.
4. Selecione a pasta do repositório clonado.
5. Clique em **Finish**.

---

## 🚀 Uso

Abra o terminal na pasta do projeto (ou use o terminal do Eclipse) e execute:

### Para simular a arquitetura:

```bash
java ArchitectureD3 <ENTRADA>
```

### Para simular o assembler:

```bash
java Assembler <ENTRADA>
```

**IMPORTANTE:**  
O parâmetro `<ENTRADA>` deve ser o nome de um arquivo `.dxf` (sem a extensão). Exemplo:

```bash
java ArchitectureD3 program1
```

---

## 👨‍💻 Integrantes

- Gabriel Galdino
- Antonio Henrique
- Henrique Souza
- Ariel Pina
- Thiago Alcantara

---

## 📄 Licença

Este projeto é acadêmico, desenvolvido para fins educacionais no contexto da disciplina de Organização e Arquitetura de Computadores.
