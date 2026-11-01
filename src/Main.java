package simulador;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Carro carro = new Carro();
        
        // Criando o Radar com limite padrão de 60 km/h e trecho de 1000m
        Radar radar = new Radar(60.0, 1000.0);

        boolean executando = true;

        System.out.println("=================================================");
        System.out.println("   BEM-VINDO AO SIMULADOR DE TRÂNSITO & RADAR   ");
        System.out.println("=================================================");

        while (executando) {
            System.out.println("\n---------------- MENU DE CONTROLE ----------------");
            System.out.println("1  - Ligar carro");
            System.out.println("2  - Desligar carro");
            System.out.println("3  - Acelerar (+1 km/h)");
            System.out.println("4  - Acelerar rápido (+5 km/h)");
            System.out.println("5  - Acelerar rápido (+10 km/h)");
            System.out.println("6  - Diminuir velocidade (-1 km/h)");
            System.out.println("7  - Frear rápido (-5 km/h)");
            System.out.println("8  - Frear rápido (-10 km/h)");
            System.out.println("9  - Trocar marcha (0 a 6)");
            System.out.println("10 - Virar à esquerda");
            System.out.println("11 - Virar à direita");
            System.out.println("12 - Verificar painel / velocidade");
            System.out.println("13 - Passar pelo RADAR 1 (Entrada do trecho 1000m)");
            System.out.println("14 - Passar pelo RADAR 2 (Saída do trecho / Checar Multa)");
            System.out.println("0  - Sair do simulador");
            System.out.print("Escolha uma opção: ");

            String entrada = scanner.nextLine().trim();

            switch (entrada) {
                case "1":
                    carro.ligar();
                    break;

                case "2":
                    carro.desligar();
                    break;

                case "3":
                    carro.acelerar();
                    break;

                case "4":
                    acelerarMultiplo(carro, 5);
                    break;

                case "5":
                    acelerarMultiplo(carro, 10);
                    break;

                case "6":
                    carro.diminuirVelocidade();
                    break;

                case "7":
                    frearMultiplo(carro, 5);
                    break;

                case "8":
                    frearMultiplo(carro, 10);
                    break;

                case "9":
                    if (carro.isLigado()) {
                        System.out.print("Informe a marcha desejada (0 para Ponto Morto, 1 a 6): ");
                    } else {
                        System.out.println("Erro: O carro está desligado.");
                        break;
                    }
                    try {
                        int novaMarcha = Integer.parseInt(scanner.nextLine().trim());
                        carro.trocarMarcha(novaMarcha);
                    } catch (NumberFormatException e) {
                        System.out.println("Erro: Digite um número válido para a marcha.");
                    }
                    break;

                case "10":
                    carro.virar("esquerda");
                    break;

                case "11":
                    carro.virar("direita");
                    break;

                case "12":
                    carro.exibirPainel();
                    break;

                case "13":
                    radar.registrarPassagemRadar1(carro);
                    break;

                case "14":
                    radar.registrarPassagemRadar2(carro);
                    break;

                case "0":
                    executando = false;
                    System.out.println("\nEncerrando o simulador de trânsito. Até a próxima corrida!");
                    break;

                default:
                    System.out.println("Opção inválida! Escolha uma das opções do menu.");
                    break;
            }
        }

        scanner.close();
    }

    // Método utilitário para acelerar em lote respeitando cada ciclo de regra de 1 km/h
    private static void acelerarMultiplo(Carro carro, int quantidade) {
        int velInicial = carro.getVelocidade();
        for (int i = 0; i < quantidade; i++) {
            int velAntes = carro.getVelocidade();
            carro.acelerar();
            if (carro.getVelocidade() == velAntes) {
                // Bloqueado pelo limite da marcha atual ou desligado
                break;
            }
        }
        int ganho = carro.getVelocidade() - velInicial;
        if (ganho > 1) {
            System.out.println("-> Aceleração rápida aplicada: +" + ganho + " km/h no total. Velocidade atual: " + carro.getVelocidade() + " km/h.");
        }
    }

    // Método utilitário para frear em lote respeitando cada ciclo de regra de 1 km/h
    private static void frearMultiplo(Carro carro, int quantidade) {
        int velInicial = carro.getVelocidade();
        for (int i = 0; i < quantidade; i++) {
            int velAntes = carro.getVelocidade();
            carro.diminuirVelocidade();
            if (carro.getVelocidade() == velAntes) {
                // Já atingiu 0 km/h ou desligado
                break;
            }
        }
        int reducao = velInicial - carro.getVelocidade();
        if (reducao > 1) {
            System.out.println("-> Frenagem rápida aplicada: -" + reducao + " km/h no total. Velocidade atual: " + carro.getVelocidade() + " km/h.");
        }
    }
}
