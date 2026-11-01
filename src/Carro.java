package simulador;

public class Carro {
    private boolean ligado;
    private int velocidade;
    private int marcha; // 0 = Ponto Morto (Neutro), 1 a 6 = Marchas

    public Carro() {
        // Regra: "Quando o carro for criado ele deve começar desligado, em ponto morto e com sua velocidade em 0"
        this.ligado = false;
        this.velocidade = 0;
        this.marcha = 0;
    }

    // Regra: "O carro desligado não pode realizar nenhuma função"
    public boolean isLigado() {
        return ligado;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public int getMarcha() {
        return marcha;
    }

    public void ligar() {
        if (ligado) {
            System.out.println("Aviso: O carro já está ligado.");
            return;
        }
        ligado = true;
        System.out.println("Carro ligado com sucesso!");
    }

    // Regra: "O carro podera ser desligado se estiver em ponto morto (marcha 0) e sua velocidade em 0 km"
    public void desligar() {
        if (!ligado) {
            System.out.println("Aviso: O carro já está desligado.");
            return;
        }
        if (marcha != 0 || velocidade != 0) {
            System.out.println("Erro: Para desligar, o carro deve estar em ponto morto (marcha 0) e parado (0 km/h).");
            return;
        }
        ligado = false;
        System.out.println("Carro desligado com sucesso!");
    }

    // Regras de limites de velocidade por marcha
    public int getVelocidadeMaximaDaMarcha(int m) {
        switch (m) {
            case 0: return 0;
            case 1: return 20;
            case 2: return 40;
            case 3: return 60;
            case 4: return 80;
            case 5: return 100;
            case 6: return 120;
            default: return 0;
        }
    }

    // Regra: "Quando o carro for acelerado ele deve incrementar 1km em sua velocidade (pode chegar no máximo a 120km)"
    // Regra: "se o carro estiver na marcha 0 (ponto morto) ele não pode acelerar"
    public void acelerar() {
        if (!ligado) {
            System.out.println("Erro: O carro está desligado.");
            return;
        }
        if (marcha == 0) {
            System.out.println("Erro: Não é possível acelerar em ponto morto (marcha 0). Engate a 1ª marcha.");
            return;
        }

        int maxPermitido = getVelocidadeMaximaDaMarcha(marcha);
        if (velocidade >= maxPermitido) {
            System.out.println("Erro: Limite da " + marcha + "ª marcha atingido (" + maxPermitido + " km/h). Passe para a próxima marcha para acelerar mais.");
            return;
        }

        velocidade++;
        System.out.println("Acelerou! Velocidade atual: " + velocidade + " km/h (Marcha " + marcha + ")");
    }

    // Regra: "Quando diminuir a velocidade do carro ele deve decrementar 1 km de sua velocidade (pode chegar no minimo a 0km)"
    public void diminuirVelocidade() {
        if (!ligado) {
            System.out.println("Erro: O carro está desligado.");
            return;
        }
        if (velocidade <= 0) {
            System.out.println("Aviso: O carro já está completamente parado (0 km/h).");
            return;
        }

        velocidade--;
        System.out.println("Freou/desacelerou! Velocidade atual: " + velocidade + " km/h (Marcha " + marcha + ")");
    }

    // Regra: "o carro deve possuir 6 marchas, não deve ser permitido pular uma marcha no carro"
    public void trocarMarcha(int novaMarcha) {
        if (!ligado) {
            System.out.println("Erro: O carro está desligado.");
            return;
        }
        if (novaMarcha < 0 || novaMarcha > 6) {
            System.out.println("Erro: Marcha inválida! O carro possui marchas de 0 (ponto morto) a 6.");
            return;
        }
        if (novaMarcha == marcha) {
            System.out.println("Aviso: O carro já está na marcha " + (marcha == 0 ? "Ponto Morto" : marcha + "ª") + ".");
            return;
        }
        // Não permitir pular marcha (diferença deve ser de exatamente 1)
        if (Math.abs(novaMarcha - marcha) != 1) {
            System.out.println("Erro: Não é permitido pular marchas! Marcha atual: " + marcha + ", solicitada: " + novaMarcha);
            return;
        }

        // Validação ao subir de marcha:
        if (novaMarcha > marcha) {
            // Se estiver saindo do ponto morto (0) para 1ª, o carro precisa estar parado (0 km/h)
            if (marcha == 0 && novaMarcha == 1) {
                // permitida a entrada da 1ª marcha a 0 km/h
            } else {
                // Para subir (ex: 1->2), a velocidade atual precisa ter atingido a velocidade máxima da marcha anterior
                int maxMarchaAnterior = getVelocidadeMaximaDaMarcha(marcha);
                if (velocidade < maxMarchaAnterior) {
                    System.out.println("Erro: Para engatar a " + novaMarcha + "ª marcha, alcance primeiro o limite da marcha atual (" + maxMarchaAnterior + " km/h). Velocidade atual: " + velocidade + " km/h.");
                    return;
                }
            }
        } 
        // Validação ao reduzir marcha:
        else {
            // A velocidade atual não pode ser superior à velocidade máxima suportada pela marcha que vai entrar
            int maxNovaMarcha = getVelocidadeMaximaDaMarcha(novaMarcha);
            if (velocidade > maxNovaMarcha) {
                System.out.println("Erro: Velocidade muito alta (" + velocidade + " km/h) para engatar a " + (novaMarcha == 0 ? "Ponto Morto" : novaMarcha + "ª marcha") + ". Reduza para " + maxNovaMarcha + " km/h.");
                return;
            }
        }

        marcha = novaMarcha;
        System.out.println("Marcha alterada com sucesso para: " + (marcha == 0 ? "Ponto Morto" : marcha + "ª marcha"));
    }

    // Regra: "O carro só pode virar para esquerda/direita se sua velocidade for de no mínimi 1km e no máximo 40km"
    public void virar(String direcao) {
        if (!ligado) {
            System.out.println("Erro: O carro está desligado.");
            return;
        }
        if (velocidade < 1 || velocidade > 40) {
            System.out.println("Erro: Para virar, a velocidade deve estar entre 1 km/h e 40 km/h. Velocidade atual: " + velocidade + " km/h.");
            return;
        }
        System.out.println("Virou com segurança para a " + direcao + " a uma velocidade de " + velocidade + " km/h.");
    }

    public void exibirPainel() {
        System.out.println("----------------------------------------");
        System.out.println("         PAINEL DO VEÍCULO             ");
        System.out.println(" Status     : " + (ligado ? "LIGADO" : "DESLIGADO"));
        System.out.println(" Velocidade : " + velocidade + " km/h");
        System.out.println(" Marcha     : " + (marcha == 0 ? "Ponto Morto (0)" : marcha + "ª marcha"));
        System.out.println("----------------------------------------");
    }
}