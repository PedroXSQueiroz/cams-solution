echo starting hub attached to resource server $RESOURCE_SERVER_URL

echo '----------------------------------'
echo 'verifying rerource server is up!'
echo '----------------------------------'

until  nc -zv $RESOURCE_SERVER_HOST $RESOURCE_SERVER_PORT
do
  echo "Waiting for resource server response..."
  # wait for 5 seconds before check again
  sleep 5
done

echo '----------------------------------'
echo 'data resource server is up'
echo '----------------------------------'

echo 'starting hub'

cd /hub/ && npm install

nodemon /hub/app/index.js $RESOURCE_SERVER_URL