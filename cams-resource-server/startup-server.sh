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

echo 'starting hub'

cd /server && \
mvn spring-boot:run -Dspring-boot.run.profiles=$PROFILE  -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=$DEBUG_PORT"