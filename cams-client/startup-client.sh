echo '###################################################'
echo 'INSTALL PACKAGES'
echo '###################################################'

npm cache clean --force
npm install

echo '###################################################'
echo 'INSTALL PACKAGES FINSHED'
echo '###################################################'

echo '----------------------------------'
echo 'verifying rerource server is up!'
echo '----------------------------------'

until  nc -zv $RESOURCE_HOST $RESOURCE_PORT
do
  echo "Waiting for resource server response..."
  # wait for 5 seconds before check again
  sleep 5
done

echo '----------------------------------'
echo 'data resource server is up'
echo '----------------------------------'

export RESOURCE_IP=$( getent hosts $RESOURCE_HOST | awk '{ print $1 }' )

export HUB_IP=$( getent hosts $HUB_HOST | awk '{ print $1 }' )

REACT_APP_RESOURCE_URL=$RESOURCE_PROTOCOL://$RESOURCE_IP:$RESOURCE_PORT REACT_APP_HUB_HOST=$HUB_IP REACT_APP_HUB_OUT_PORT=$HUB_OUT_PORT PORT=$FRONT_PORT npm run start