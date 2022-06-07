import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class BatalhaNavalRedes {
    static int[][] tabuleiro1, tabuleiro3;//Tabulero 1 vai ser para o Local (Cliente) e o 3 para o Host;
    static int quantidadeDeNavios = 5;//Quantidade de navios que será usada na hora de encher o tabuleiro;
    static int naviosRestantesparaAbater = 5;//Navios restantes para que o Cliente vença;
    static int naviosRestantesNoTabuleiro = 5;//Navios restantes para que o Servidor Vença;

    static Scanner teclado = new Scanner(System.in);

    //Inicia os tabuleiros e limpa eles, zerando todos os valores;
    public static void iniciarTabuleiros() {
        tabuleiro1 = new int[5][5];
        //tabuleiro2 = new int[5][5];
        tabuleiro3 = new int[5][5];
        limparTabuleiro(tabuleiro1);
        //limparTabuleiro(tabuleiro2);
        limparTabuleiro(tabuleiro3);
    }

    //Limpa os tabuleiros, zerando todos os valores;
    public static void limparTabuleiro(int[][] tabuleiro) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                tabuleiro[i][j] = 0;
            }
        }
    }

    //Mostra o mapa das coordenadas da matriz
    private static void mostrarMapa() {
        System.out.println("-------------------");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(i + 1 + ",");
                System.out.print(j + 1 + "  ");
            }
            System.out.println();
        }
        System.out.println("-------------------");
    }

    //Mostra as mensagens de ajuda e orientação
    private static void mostrarAjuda() {
        System.out.println("Legenda do seu campo:");
        System.out.println("NNN: Seu barco");
        System.out.println("xxx: Seu barco destruido");
        System.out.println("---------------------");
        System.out.println("Legenda do campo do oponente");
        System.out.println("ooo: Tiro errado");
        System.out.println("XXX: Tiro certo");
        System.out.println("---------------------");
    }

    //Imprime os dois tabuleiros
    private static void imprimirTabuleiros() {
        int numeroDalinha = 1, numeroDaColuna = 1;

        System.out.println("------------------------------------------------");
        System.out.println("       SEU CAMPO           CAMPO DO OPONENTE");
        for (int i = 0; i < 5; i++) {
            System.out.print("|");
            for (int j = 0; j < 5; j++) {
                switch (tabuleiro1[i][j]) {
                    //0 representa sem interação
                    case 0: {
                        System.out.print("--- ");
                        break;
                    }
                    //Quando tiver um navio
                    case 1: {
                        System.out.print("NNN ");
                        break;
                    }
                    //Quando Acertar
                    case 2: {
                        System.out.print("XXX ");
                        break;
                    }
                    //Quando errar
                    case 3: {
                        System.out.print("ooo ");
                        break;
                    }
                }
            }
            System.out.print("|  |");
            for (int k = 0; k < 5; k++) {
                switch (tabuleiro3[i][k]) {
                    //0 representa sem interação
                    case 0: {
                        System.out.print("--- ");
                        break;
                    }
                    //Quando tiver um navio
                    case 1: {
                        System.out.print("NNN ");
                        break;
                    }
                    //Quando Acertar
                    case 2: {
                        System.out.print("XXX ");
                        break;
                    }
                    //Quando errar
                    case 3: {
                        System.out.print("ooo ");
                        break;
                    }
                }
            }
            System.out.println("|");
        }
        System.out.println("------------------------------------------------");
        mostrarMapa();
        mostrarAjuda();
    }

    //Assim como em Python, aqui acontece um calculo usando o random.nextInt. Pegando um inteiro entre 1 e 100. Se ele for menor que 10 (10% de chance) ele salva a posição com um barco e decrementa o contador
    public static void colocarNaviosNoTabuleiro(int[][] tabuleiro) {

        limparTabuleiro(tabuleiro);
        int quantidadeRestante = quantidadeDeNavios;
        Random random = new Random();
        do {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (random.nextInt(100) <= 10) {
                        if (tabuleiro[i][j] == 0) {
                            tabuleiro[i][j] = 1;
                            quantidadeRestante--;
                            break;
                        }
                        if (quantidadeRestante <= 0) {
                            break;
                        }
                    }
                    if (quantidadeRestante <= 0) {
                        break;
                    }
                }
            }
        } while (quantidadeRestante > 0);
    }

    //MAIN
    public static void main(String[] args) throws IOException {

        System.out.println("+----------------------------------------------+");
        System.out.println("|                C O M E Ç O U!                |");
        System.out.println("|   Cada navio ocupa uma posição do tabuleiro  |");
        System.out.println("+----------------------------------------------+");
        iniciarTabuleiros();
        colocarNaviosNoTabuleiro(tabuleiro1);
        naviosRestantesparaAbater = quantidadeDeNavios;
        naviosRestantesNoTabuleiro = quantidadeDeNavios;
        imprimirTabuleiros();
        boolean acabou = false;//Condição para dizer se o jogo acabou ou não

        //Variáveis utilizadas na comunicação do Sockets
        String resposta = null;
        String msg = null;
        String host;
        int port;
        int len;
        Socket client = null;
        InputStream entrada = null;
        OutputStream saida = null;
        ByteArrayOutputStream os = null;
        Scanner teclado = new Scanner(System.in);
        boolean minhavez = false;
        byte[] buffer = null;

        //Host e porta
        //Colocar o IPV4 do HOST
        host = "localhost";
        port = 32000;

        client = new Socket(host, port);//Instancia um novo Socket no ip e porta informados
        System.out.println("\nO cliente se conectou ao servidor \n");

        while (!acabou) {//Enquanto não for o fim d jogo
            if (minhavez) {//confere se é a vez do Cliente ou do Host. o jogo inicia na vez do HOST, então vá para o próximo else
                imprimirTabuleiros();
                try {
                    System.out.println("Deseja atacar qual posição:");
                    assert false;
                    msg = teclado.nextLine();//recebe o que o usuário digitar
                    // Escreve array de bytes no stream de saida
                    saida = client.getOutputStream();
                    saida.write(msg.getBytes("UTF-8"));

                    System.out.println("Esperando resposta...");
                    entrada = client.getInputStream();//recebe o que o host enviar e converte os dados para String
                    buffer = new byte[1024];
                    os = new ByteArrayOutputStream();
                    while ((len = entrada.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                        break;
                    }
                    resposta = os.toString();//Salva em uma String "resposta" para enviar para a "checaResposta" e verificar se acertou ou errou o disparo
                    //checa se acertou
                    checaResposta(resposta, msg);
                    if (naviosRestantesNoTabuleiro == 0 || naviosRestantesparaAbater == 0) {//Se alguem abater os 5, o jogo acaba
                        acabou = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (naviosRestantesNoTabuleiro == 0 || naviosRestantesparaAbater == 0) {
                    acabou = true;
                }
                minhavez = false;
            } else {//O JOGO COMEÇA POR AQUI
                System.out.println("Esperando movimento do oponente...");
                // Prepara o stream de entrada
                entrada = client.getInputStream();
                buffer = new byte[1024];
                os = new ByteArrayOutputStream();
                // Joga o resultado da entrada em um ByteArrayOutputStream
                while ((len = entrada.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                    break;
                }
                System.out.println("Jogada Recebida foi: " + os + "\n");
                resposta = os.toString();
                //checa se acertou
                checaAcerto(resposta, saida, client, buffer, entrada);
                minhavez = true;
                if (naviosRestantesNoTabuleiro == 0 || naviosRestantesparaAbater == 0) {
                    acabou = true;
                }
            }
            if (naviosRestantesNoTabuleiro == 0 || naviosRestantesparaAbater == 0) {
                acabou = true;
            }
        }

        if (naviosRestantesNoTabuleiro == 0) {
            System.out.println("*********************************************************");
            System.out.println("\t\tVocê Perdeu!!");
            System.out.println("*********************************************************");
            imprimirTabuleiros();
        } else {
            System.out.println("*********************************************************");
            System.out.println("\t\tParabéns! Você ganhou!!");
            System.out.println("*********************************************************");
            imprimirTabuleiros();
        }

        assert saida != null;
        saida.close();
        entrada.close();
        os.close();
        client.close();
        teclado.close();
    }

    //Checa a resposta recebida do ataque. "acertou" ou "errou" Depois salva no tabuleiro com os valores correspondentes
    private static void checaResposta(String resposta, String msg) {
        int x, y;
        String[] msg2 = msg.replaceAll("\\[", "")
                .replaceAll("]", "").split(",");
        int[] intArray = new int[msg2.length];
        for (int i = 0; i < msg2.length; i++) {

            try {
                intArray[i] = Integer.parseInt(msg2[i]);
            } catch (Exception e) {
                System.out.println("Unable to parse string to int: " + e.getMessage());
            }
        }
        x = intArray[0];
        y = intArray[1];
        x--;
        y--;
        if (resposta.equals("acertou")) {
            System.out.println("Você acertou um tiro!");
            tabuleiro3[x][y] = 2;
            imprimirTabuleiros();
            naviosRestantesparaAbater--;
        }
        if (resposta.equals("errou")) {
            System.out.println("Você errou o tiro");
            tabuleiro3[x][y] = 3;
            imprimirTabuleiros();
        }
        if (resposta.equals("repetiu")) {
            System.out.println("Você repetiu uma jogada anterior");
        }
    }

    //Checa se os valores enviados pelo HOST, acertaram ou erraram. Depois retorna com as mensagens "acertou" ou "errou"
    private static void checaAcerto(String resposta, OutputStream saida, Socket client, byte[] buffer, InputStream entrada) throws IOException {
        int x, y;
        String msgDeAcerto;
        String[] msg2 = resposta.replaceAll("\\[", "")
                .replaceAll("]", "").split(",");
        int[] intArray = new int[msg2.length];

        for (int i = 0; i < msg2.length; i++) {
            try {
                intArray[i] = Integer.parseInt(msg2[i]);
            } catch (Exception e) {
                System.out.println("Unable to parse string to int: " + e.getMessage());
            }
        }
        x = intArray[0];
        y = intArray[1];
        x--;
        y--;
        if ((tabuleiro1[x][y]) == 1) {
            System.out.println("O Oponente acertou");
            assert false;
            msgDeAcerto = "acertou";
            // Escreve array de bytes no stream de saida
            saida = client.getOutputStream();
            saida.write(msgDeAcerto.getBytes("UTF-8"));
            //define acerto
            tabuleiro1[x][y] = 2;
            naviosRestantesNoTabuleiro--;
        } else if ((tabuleiro1[x][y]) == 0 || (tabuleiro1[x][y]) == 3) {
            System.out.println("O Oponente errou");
            assert false;
            tabuleiro1[x][y] = 3;
            msgDeAcerto = "errou";
            // Escreve array de bytes no stream de saida
            saida = client.getOutputStream();
            saida.write(msgDeAcerto.getBytes("UTF-8"));
        } else if ((tabuleiro1[x][y]) == 2) {
            System.out.println("Já disparou nessa posição. Perdeu a vez!");
            assert false;
            msgDeAcerto = "acertou";
            // Escreve array de bytes no stream de saida
            saida = client.getOutputStream();
            saida.write(msgDeAcerto.getBytes("UTF-8"));
        }
    }
}
