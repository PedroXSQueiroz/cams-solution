echo '----------------------------------'
echo 'verifying messaging server is up!'
echo '----------------------------------'

until nc -z -v -w30 $MESSAGING_BROKER_URL $MESSAGING_BROKER_PORT
do
  echo "Waiting for messaging server response..."
  # wait for 5 seconds before check again
  sleep 5
done

echo '----------------------------------'
echo 'messaging server is up'
echo '----------------------------------'

if [[ -z $DATABASE_SERVER_URL || -z $DATABASE_SERVER_PORT ]]
then

  echo '----------------------------------'
  echo 'using local database!'
  echo '----------------------------------'

else
  
  echo '----------------------------------'
  echo "requires database server, verifying database server is up $DATABASE_SERVER_URL:$DATABASE_SERVER_PORT!"
  echo '----------------------------------'

  until nc -z -v -w30 $DATABASE_SERVER_URL $DATABASE_SERVER_PORT
  do
    echo "Waiting for database server response..."
    # wait for 5 seconds before check again
    sleep 5
  done

  echo '----------------------------------'
  echo 'database server is up!'
  echo '----------------------------------'

fi

echo 'starting hub'

cd /server && \
mvn spring-boot:run -Dspring-boot.run.profiles=$PROFILE  -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=$DEBUG_PORT"