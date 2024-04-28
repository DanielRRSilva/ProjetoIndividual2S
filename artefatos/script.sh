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
ls BackEnd/

if [ $? = 0 ]
    then
        echo "Diretório Encontrdado!"
else
    echo "diretório não encontrado!"
    echo "Importando aplicação..."
    git clone https://github.com/Idea7-2ADSA/BackEnd.git
fi

cp BackEnd/IdeaDataBridge/src/artefatos/IdeaDataBridge-1.0-SNAPSHOT-jar-with-dependencies.jar ~/

chmod 555 IdeaDataBridge-1.0-SNAPSHOT-jar-with-dependencies.jar

rm -r -f BackEnd/
 
#Configurando MySQL
echo "Instalando banco de dados..."

sudo apt install mysql-server -y
sudo systemctl start mysql
sudo systemctl enable mysql

ls Data/

if [ $? = 0 ]
    then
        echo "diretório encontrado"
else
    echo "diretório não encontrado"
    echo "Importando banco de dados..."
    git clone https://github.com/Idea7-2ADSA/Data.git
fi

cp Data/script_banco_idea7.sql ~/
rm -r -f Data/

SCRIPT_MYSQL="script_banco_idea7.sql"

sudo mysql < "$SCRIPT_MYSQL"

echo "Maquina configurada!"