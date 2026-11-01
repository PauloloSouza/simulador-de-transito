# Simulador de Trânsito & Controle de Carro

Projeto prático em Java que une o exercício de controle de funções e regras de transmissão de um carro com o sistema de fiscalização de velocidade média (**Traffic Radar**).

---

## 🚗 Regras do Veículo

1. **Estado Inicial:**
   - O carro inicia **desligado**, em **ponto morto (marcha 0)** e com **velocidade em 0 km/h**.
2. **Funções com Veículo Desligado:**
   - Nenhuma função de aceleração, desaceleração, curva ou troca de marcha pode ser realizada com o carro desligado.
3. **Ligar e Desligar:**
   - Para desligar, o carro obrigatoriamente deve estar em **ponto morto (marcha 0)** e com **velocidade em 0 km/h**.
4. **Aceleração e Limites por Marcha:**
   - Cada aceleração padrão incrementa **+1 km/h** na velocidade (teto máximo de 120 km/h).
   - O menu conta também com atalhos de aceleração contínua de **+5 km/h** e **+10 km/h** que respeitam os tetos da marcha.
   - Não é possível acelerar em ponto morto (marcha 0).
   - **Tabela de Marchas:**
     - `Ponto Morto (0)`: 0 km/h (sem aceleração)
     - `1ª Marcha`: 0 a 20 km/h
     - `2ª Marcha`: 21 a 40 km/h
     - `3ª Marcha`: 41 a 60 km/h
     - `4ª Marcha`: 61 a 80 km/h
     - `5ª Marcha`: 81 a 100 km/h
     - `6ª Marcha`: 101 a 120 km/h
5. **Troca de Marchas:**
   - Não é permitido pular marchas (diferença estrita de 1 marcha por vez).
   - Para subir de marcha (ex: 1ª para 2ª), é necessário atingir o limite de velocidade da marcha atual (ex: 20 km/h).
   - Para reduzir marcha, a velocidade atual já deve ter sido reduzida até o teto da marcha pretendida.
6. **Curvas:**
   - O veículo só pode virar para a esquerda ou direita se a velocidade estiver entre **1 km/h** e **40 km/h**.

---

## 📸 Sistema de Radar (Traffic Radar)

- **Distância do Trecho:** 1.000 metros entre o Radar 1 e o Radar 2.
- **Limite da Via:** 60 km/h.
- **Tolerância:** 10% (66,0 km/h).
- **Medição do Trecho:**
  - Ao passar pelo **Radar 1**, é registrada a velocidade instantânea e iniciado o cronômetro com `System.nanoTime()`.
  - Ao passar pelo **Radar 2**, o sistema calcula o tempo decorrido, a velocidade pontual e a **velocidade média real** no percurso de 1 km.
  - Se o condutor demorar menos tempo que o tempo mínimo regulamentar ou passar acima da tolerância pontual, uma infração de trânsito é emitida.

---

## 🛠️ Como Executar

Na pasta raiz do projeto:

```bash
# Compilar
javac -d bin src/*.java

# Executar
java -cp bin simulador.Main
```
