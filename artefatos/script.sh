#!bin/bash

#atualizando ubuntu
sudo apt update

#criando novo usuario
yes | sudo adduser cliente_idea
echo "cliente_idea:cliente_password" | chpasswd

#verificando java
echo "Verificando java..."
java -version
if [ $? = 0 ];
    then
        echo "java já instalado!"
    else
        echo "instalando a jre..."
        sudo apt install openjdk-17-jre -y
        echo "jre instalada"
fi

echo "Instalando aplicação..."
ls ProjetoIndividual2S/

if [ $? = 0 ]
    then
        echo "Diretório Encontrdado!"
else
    echo "diretório não encontrado!"
    echo "Importando aplicação..."
    git clone https://github.com/DanielRRSilva/ProjetoIndividual2S.git
fi

cp ProjetoIndividual2S/artefatos/individual-1.0-SNAPSHOT-jar-with-dependencies.jar ~/

chmod 555 individual-1.0-SNAPSHOT-jar-with-dependencies.jar

 
#Configurando MySQL
echo "Instalando banco de dados..."

sudo apt install mysql-server -y
sudo systemctl start mysql
sudo systemctl enable mysql

cp ProjetoIndividual2S/data/script_banco_individual.sql ~/

SCRIPT_MYSQL="script_banco_individual.sql"

sudo mysql < "$SCRIPT_MYSQL"

rm -r -f ProjetoIndividual2S/

echo "Maquina configurada!"