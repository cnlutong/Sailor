package de.luandtong.sailor.domian.wg;

//#client1
//[Interface]
//PrivateKey = YB6e40vMrTucBgeGrgUAxhb4KdyENhE4VXSPGT57vXs=
//Address = 10.10.0.3/24
//DNS = 1.1.1.1, 1.0.0.1
//
//        [Peer]
//PublicKey = null
//Endpoint = null:51820
//AllowedIPs = 0.0.0.0/0
//PersistentKeepalive = 15
public class ClientInterface implements WGInterface {

    private String address;
    private String dns;

    private String persistentKeepalive;


    @Override
    public String creativeInterfaceConfFile() {
        return null;
    }


}
