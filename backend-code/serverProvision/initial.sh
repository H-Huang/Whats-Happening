echo $1 #projectName
echo $2 #projectUser
echo $3 #password

sudo apt-get update -y

# git
sudo apt-get install git -y

# Python
sudo apt-get install python3 -y
sudo apt-get install python3-pip -y
sudo apt-get install python3.4-dev -y

# Nginx setup
sudo apt-get install nginx -y
sudo mv ./$1 /etc/nginx/sites-available/$1
sudo ln -s /etc/nginx/sites-available/$1 /etc/nginx/sites-enabled
sudo service nginx restart

#supervisor setup
sudo apt-get install supervisor -y
sudo cp ./gunicorn.conf /etc/supervisor/conf.d/gunicorn.conf
sudo service supervisor restart

#postgresql
sudo apt-get install libpq-dev -y
sudo apt-get install postgresql -y
sudo apt-get install postgresql-contrib -y

#create the database
sudo -u postgres -i psql -c "CREATE DATABASE $1;"
sudo -u postgres -i psql -c "CREATE USER $2 WITH PASSWORD '$3';"
sudo -u postgres -i psql -c "GRANT ALL PRIVILEGES ON DATABASE $1 TO $2;"
sudo -u postgres -i psql -c "ALTER ROLE $2 SET client_encoding TO 'utf8';"
sudo -u postgres -i psql -c "ALTER ROLE $2 SET default_transaction_isolation TO 'read committed';"
sudo -u postgres -i psql -c "ALTER ROLE $2 SET timezone TO 'UTC';"

#upgrade pip
sudo pip3 install --upgrade pip

#creating virtual env
sudo pip3 install virtualenv
cd ~
virtualenv $1
source ~/$1/bin/activate

#django, gunicorn, psycopg2
pip install django
pip install gunicorn
pip install psycopg2

#project specific packages
pip install djangorestframework
