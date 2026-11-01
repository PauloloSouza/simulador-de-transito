package simulador;

public class Radar {
    private double limiteVelocidade;       // em km/h (ex: 60.0)
    private double distanciaMetros;        // em metros (agora 1000m)
    private double limiteTolerancia;       // limite + 10% de tolerância
    private double tempoMinimoSegundos;    // tempo mínimo legal para percorrer o trecho sem excesso de média
    
    private long tempoPassagemRadar1Nano;  // marcação de tempo ao passar no 1º radar
    private boolean passouRadar1;

    public Radar(double limiteVelocidade, double distanciaMetros) {
        this.limiteVelocidade = limiteVelocidade;
        this.distanciaMetros = distanciaMetros;
        this.limiteTolerancia = limiteVelocidade * 1.10; // 10% de tolerância
        
        // Conversão de km/h para m/s: v(m/s) = v(km/h) / 3.6
        double metrosPorSegundoPermitido = limiteVelocidade / 3.6;
        // Tempo mínimo permitido em segundos: t = d / v
        this.tempoMinimoSegundos = distanciaMetros / metrosPorSegundoPermitido;
        this.passouRadar1 = false;
    }

    public double getLimiteVelocidade() {
        return limiteVelocidade;
    }

    public double getDistanciaMetros() {
        return distanciaMetros;
    }

    public double getTempoMinimoSegundos() {
        return tempoMinimoSegundos;
    }

    // Registra a passagem pelo primeiro radar
    public void registrarPassagemRadar1(Carro carro) {
        if (!carro.isLigado()) {
            System.out.println("Erro: O carro está desligado.");
            return;
        }

        double vel = carro.getVelocidade();
        this.tempoPassagemRadar1Nano = System.nanoTime();
        this.passouRadar1 = true;

        System.out.println("\n========================================");
        System.out.println("   [RADAR 1 - ENTRADA DO TRECHO]");
        System.out.println("   Distância até o Radar 2: " + distanciaMetros + " metros");
        System.out.println("   Limite da via: " + limiteVelocidade + " km/h (Tolerância: " + String.format("%.1f", limiteTolerancia) + " km/h)");
        System.out.println("   Velocidade registrada: " + vel + " km/h");

        // Avaliação pontual no Radar 1
        avaliarVelocidadePontual(vel);
        System.out.println("========================================\n");
    }

    // Registra a passagem pelo segundo radar (cálculo de velocidade média no trecho)
    public void registrarPassagemRadar2(Carro carro) {
        if (!carro.isLigado()) {
            System.out.println("Erro: O carro está desligado.");
            return;
        }
        if (!passouRadar1) {
            System.out.println("Aviso: O carro ainda não passou pelo Radar 1 para iniciar a medição do trecho.");
            return;
        }

        long tempoPassagemRadar2Nano = System.nanoTime();
        double vel = carro.getVelocidade();
        
        // Tempo decorrido em segundos
        long nanoDecorrido = tempoPassagemRadar2Nano - tempoPassagemRadar1Nano;
        double segundosDecorridos = nanoDecorrido / 1_000_000_000.0;
        
        // Cálculo da velocidade média real no trecho: v_media = (distancia / tempo) * 3.6
        double velMediaKmh = (segundosDecorridos > 0) ? (distanciaMetros / segundosDecorridos) * 3.6 : 0.0;

        System.out.println("\n========================================");
        System.out.println("   [RADAR 2 - FIM DO TRECHO MONITORADO]");
        System.out.println("   Velocidade pontual no Radar 2: " + vel + " km/h");
        System.out.printf("   Tempo no trecho de %.0fm: %.2f segundos\n", distanciaMetros, segundosDecorridos);
        System.out.printf("   Tempo mínimo permitido: %.2f segundos\n", tempoMinimoSegundos);
        System.out.printf("   Velocidade média calculada: %.2f km/h\n", velMediaKmh);
        System.out.println("----------------------------------------");

        // Avaliação pontual no Radar 2
        boolean infracaoPontual = vel > limiteTolerancia;
        boolean alertaPontual = vel > limiteVelocidade && vel <= limiteTolerancia;

        // Avaliação da média do trecho
        boolean infracaoMedia = segundosDecorridos < tempoMinimoSegundos;

        if (infracaoPontual || infracaoMedia) {
            System.out.println(" [!] VOCÊ FOI MULTADO!");
            if (infracaoPontual) {
                System.out.printf("   - Excesso pontual no sensor: %.1f km/h (tolerância: %.1f km/h)\n", vel, limiteTolerancia);
            }
            if (infracaoMedia) {
                System.out.printf("   - Excesso na média do trecho: %.2f km/h acima do limite de %.0f km/h\n", velMediaKmh, limiteVelocidade);
                System.out.printf("   - Você percorreu o trecho rápido demais (%.2fs vs mín. %.2fs permitidos)!\n", segundosDecorridos, tempoMinimoSegundos);
            }
        } else if (alertaPontual) {
            System.out.println(" [!] ATENÇÃO: Acima do limite da via, mas dentro da tolerância de 10%. Reduza a velocidade!");
        } else {
            System.out.println(" [OK] PARABÉNS! Você respeitou os limites pontuais e a média do trecho.");
        }
        System.out.println("========================================\n");

        // Reseta o trecho para nova simulação se desejar
        passouRadar1 = false;
    }

    private void avaliarVelocidadePontual(double vel) {
        if (vel > limiteTolerancia) {
            System.out.printf("   [!] MULTA: Você passou acima da tolerância permitida (%.1f km/h)!\n", limiteTolerancia);
        } else if (vel > limiteVelocidade) {
            System.out.printf("   [!] ATENÇÃO: Acima do limite (%.0f km/h), mas dentro dos 10%% de tolerância.\n", limiteVelocidade);
        } else {
            System.out.println("   [OK] Velocidade dentro do limite da via.");
        }
    }

    public boolean isPassouRadar1() {
        return passouRadar1;
    }
}
