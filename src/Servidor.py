import random
import re
import socket
from os import system
import numpy as np

# colocar o endereço ipv4 do HOST
HOST = 'localhost'
#HOST = '192.168.0.182'
PORT = 32000

matrizP1 = np.zeros((5, 5))
visaoP2 = np.zeros((5, 5))
mapa = [['1,1', '1,2', '1,3', '1,4', '1,5'],
        ['2,1', '2,2', '2,3', '2,4', '2,5'],
        ['3,1', '3,2', '3,3', '3,4', '3,5'],
        ['4,1', '4,2', '4,3', '4,4', '4,5'],
        ['5,1', '5,2', '5,3', '5,4', '5,5']]


# Adiciona os barcos no tabuleiro. Esse método adiociona de forma randomica 5 barcos.
def adicionaBarcos():
    quantidadeRestante = 5  # Contador de barcos restantes

    while (quantidadeRestante > 0):  # Loop enquanto houverem navios restantes para colocar no tabuleiro
        for i in range(5):
            for j in range(5):
                x = random.randint(1, 100)  # x recebe um valor aletório entre 1 e 100
                if (
                        x <= 10):  # Caso esse valor seja menor ou igual a 10, ele entra na condição. Fazendo assim com que cada posição tenha 10% de chance de entrar
                    if (matrizP1[i][j] == 0):  # Se a posição não tiver nenhum barco ( ==1 ) ele entra
                        matrizP1[i][j] = 1  # Adiciona o número 1 que representa o barco
                        quantidadeRestante = quantidadeRestante - 1  # Diminui 1 do contador de barcos
                        break
                    if (quantidadeRestante <= 0):  # condição de parada
                        break
            if (quantidadeRestante <= 0):  # condição de parada
                break
        if (quantidadeRestante <= 0):  # condição de parada
            break


# método para limpar o terminal
def clear():
    system('cls')


# Verifica se o tiro é válido ou não
def tiroValido(pos):
    s = re.sub('\d', '$', pos)
    if (s == '$,$'):
        x = int(pos[0])
        y = int(pos[2])
        x = x - 1  # Todas as vezes que aparecer algo do tipo x = x-1... É para converter a posição inserida pelos jogadores, em posição de matriz. logo um x=5, y=1. Vira um x=4, y=0
        y = y - 1
        if ((x >= 0) and (x <= 4) and (y >= 0) and (
                y <= 4)):  # verifica se os valores de x e y estão dentro do limite da matriz (5x5) Sendo que começa no índice 0
            if (visaoP2[x][y] == 0):
                return True
            else:
                print('Um tiro já foi dado nessa posição')
                return False
        else:
            print('Tiro fora do mapa.')
            return False
    else:
        print('Formato inválido.')
        return False


# Esse método vai imprimir os dois tabuleiros
def renderizar():
    for i in range(16):
        print('---', end='')
    print('\n')
    print("       SEU CAMPO           CAMPO DO OPONENTE")
    for i in range(5):
        print('|', end='')
        for j in range(5):
            if (matrizP1[i, j] == 0):
                print('--- ', end='')
            if (matrizP1[i, j] == 1):  # Possui barco
                print('NNN ', end='')
            if (matrizP1[i, j] == 2):  # Possui barco destruido
                print('XXX ', end='')
            if (matrizP1[i, j] == 3):  # Errou
                print('ooo ', end='')
        print('|  |', end='')
        for j in range(5):
            if (visaoP2[i, j] == 0):
                print('--- ', end='')
            if (visaoP2[i, j] == 1):  # Errou
                print('ooo ', end='')
            if (visaoP2[i, j] == 2):  # Acertou
                print('XXX ', end='')
        print('|', end='')
        print('\n')
    for i in range(16):
        print('---', end='')
    print('')
    mostrarMapa()
    mostrarAjuda()


# Imprime o Mapa da linha 14 do código
def mostrarMapa():
    print('-------------------')
    for i in range(5):
        for j in range(5):
            print(mapa[i][j], end='')
            print('  ', end='')
        print('')
    print('-------------------')


# Mostra as posições da matriz para facilitar o entendimento
def mostrarAjuda():
    print('Legenda do seu campo:')
    print('NNN: Seu barco')
    print('xxx: Seu barco destruido')
    print('---------------------')
    print("Legenda do campo do oponente")
    print("ooo: Tiro errado")
    print("XXX: Tiro certo")
    print('---------------------')

#Mágica dos sockets acontecendo
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.settimeout(3600)
    s.bind((HOST, PORT))#Associa o ip e a porta à instância de um socket
    s.listen()#espera a resposta do cliente
    print("Esperando conexão do segundo jogador")
    conn, addr = s.accept()#confirma a conexão
    with conn:
        clear()
        adicionaBarcos()#Chama o método de adicionar os barcos

        acertosParaVencer = 5#Define a quantidade de acertos restantes para vencer
        acertosParaOponente = 5

        print("+----------------------------------------------+");
        print("|                C O M E Ç O U!                |");
        print("|   Cada navio ocupa uma posição do tabuleiro  |");
        print("+----------------------------------------------+");

        while acertosParaVencer > 0 and acertosParaOponente > 0:
            renderizar()
            print("Digite o alvo do seu tiro...")
            entrada = input()#Recebe o que o usuário digitar e salva na variável "entrada"
            while not tiroValido(entrada):#Força a fazer uma jogada válida
                entrada = input()

            # Envia a coordenada do ataque para o Cliente
            conn.sendall(entrada.encode("utf-8"))
            # Coloca as coordenadas enviadas em duas variáveis. X e Y
            x = int(entrada[0])
            y = int(entrada[2])
            x = x - 1
            y = y - 1

            #Ao enviar as coordenadas, o cliente vai validar o tiro e dizer se acertou ou errou. A variável "data" Recebe a resposta se foi "acertou" ou "errou"
            #Recebe até 1024 bytes de tamanho
            data = conn.recv(1024).decode()
            clear()

            if data == 'acertou':#Se "acertou" for a resposta recebida
                print("Você acertou!!")
                visaoP2[x][y] = 2#Salva no tabuleiro do Oponente(Cliente) que o disparo foi um acerto
                acertosParaVencer -= 1
            if data == 'errou':#Se "errou" for a resposta recebida
                print("Você errou...")
                visaoP2[x][y] = 1#Salva no tabuleiro como erro de disparo

            if acertosParaVencer == 0:#Se Já tiver acertado todos os 5 barcos, o contador chegara em 0, entrando no "if" e rodando o "break"
                break

            print("Esperando tiro do oponente...")
            renderizar()

            #Recebe as coordenadas do Cliente até 1024 bytes de tamanho
            data = conn.recv(1024).decode()
            x = int(data[0])
            y = int(data[2])
            x = x - 1
            y = y - 1

            clear()
            if matrizP1[x][y] == 1:#Confere se o que o cliente envioum corresponde à coordenada de um navio
                print("Ele acertou!!!")
                matrizP1[x][y] = 2#salva na matriz do Servidor (Python) que foi um acerto
                conn.sendall('acertou'.encode("utf-8"))#Envia para o Cliente a mensagem "acertou" para ele validar lá
                acertosParaOponente -= 1#Diminui 1 do contador de acertos restantes para o oponente vencer
            if matrizP1[x][y] == 0:#Se o disparo acertar a água...
                print("Ele errou!!!")
                matrizP1[x][y] = 3
                conn.sendall('errou'.encode("utf-8"))#Envia a mensagem "errou"

clear()
if acertosParaVencer == 0:#Se acertar os 5 disparos
    print("Você Venceu!!")
else:#caso o jogo acabe, mas foi por o Oponente ter acertado os 5 disparos
    print("Você Perdeu!!")

renderizar()
input("Pressione <enter> para encerrar!")
