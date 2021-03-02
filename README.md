# MSc-Thesis

### Installazione (versione 20.04 di Ubuntu)

CORE fornisce uno script per automatizzare l'installazione dell'ambiente virtuale. Il processo di installazione automatico è indicato al seguente [link](https://coreemu.github.io/core/install.html#automated-install). In breve i comandi da eseguire sono:
* git clone https://github.com/coreemu/core.git
*  cd core
* ./install.sh 

Dopo che l'installazione è avvenuta con successo si dovrebbe essere in grado di eseguire core-daemon e core-pygui. 

### core-daemon 

Il demone core può essere avviato direttamente da command line, utile per vedere i log generati: **sudo core-daemon --ovs**. Il flag *--ovs* permette di creare tutti gli switch Ethernet istanziati sull'ambiente CORE usando openvswitch, eseguito non nel contesto del singolo nodo. Questo significa che nella macchina host su cui è eseguito CORE è possibile vedere tutte le interfacce che definiscono la rete. Per interconnettere le interfacce dei nodi vengono utilizzate coppie di *veth*.
Installazione diretta di openvswitch:
* sudo apt update
* sudo apt upgrade
* sudo apt install openvswitch-switch

Si ha in dotazione *ovs-vsctl*, l'utility che permette di aggiornare ed interrogare il demone che implementa lo switch ovs (*ovs-vswitchd*). Utilizzando il comando **ovs-vsctl show** si ha una overview del contenuto del database di ovs, che mostra i bridge creati, di solito denominati con il prefisso *b.* seguiti da una notazione decimale puntata. Potenzialmente tutti i bridge listati possono essere connessi al controller ONOS. Il protocollo OpenFlow non è abilitato di default e per configurare un bridge (e.g b.1.2) per supportare le versioni del protocollo è necessario lanciare il comando **ovs-vsctl set bridge b.1.2 protocols=OpenFlow10,OpenFlow12,OpenFlow13,OpenFlow14,OpenFlow15** (in questo modo si abilitano tutte e 5 le versioni del protocollo). A questo punto basterà specificare il controller responsabile della configurazione dello switch: **ovs-vsctl set-controller b.1.2 tcp:localhost:6653** (supponendo che il controller SDN sia eseguito sulla stessa macchina host di CORE e la porta su cui è in ascolto OF è la default 6653).

### core-pygui

Per la GUI di CORE si utilizza la versione BETA Python GUI e non la standard, in quanto essa include il supporto per l'esecuzione di nodi basati su container Docker. Essa viene eseguita scrivendo da shell **core-pygui** (la GUI non necessita di essere eseguita come root) e si presenta in questo modo:

![](images/CORE%20GUI.png)

I nodi, raffigurati dalle icone a destra della GUI nella toolbar, sono instanziati selezionandoli e trascinandoli sulla sfondo bianco. Il nodo Docker è situato nella sezione *Container Nodes* e rappresentato da un cerchio blu:

![](images/Toolbar%20GUI%20CORE_1.png)

Il nodo Docker può essere configurato effettuando un doppio click su di esso: qui è possibile specificare nella campo *Image*  l'immagine base da cui il container verrà costruito ed eseguito (default *ubuntu:latest*). Le immagini devono essere presenti nel repository locale.

Prima di avviare l'emulazione (cliccando sul pulsante *Start Button*), è bene configurare opportunamente i container Docker per far si che sia CORE ad orchestrare la connessione tra i nodi. Bisognerà quindi eliminare la rete docker di default e disabilitare l'aggiunta delle regole iptable. Il [supporto Docker](https://github.com/coreemu/core/tree/master/daemon/examples/docker) contiene tutte le informazioni dettagliate. Importante è aggiungere nei Dockerfile i tool di networking per la configurazione del container. Oltre a quelli specificati nel link di supporto, si consiglia di aggiungere anche il package contenente il comando *ping*:
``` 
RUN apt-get update

RUN apt-get install -y iproute2 ethtool && \ 

    apt-get install -y iputils-ping
```

Durante il *docker build* è probabile che si cada nell'errore in cui apt-get non è in grado di risolvere i domini e quindi scaricare i package necessari per costruire l'immagine. Infatti accade che Docker non è in grado di trovare un server DNS definito localmente nel file */etc/resolv.conf* e il container userà di default il server DNS di Google 8.8.8.8. Utilizzando la configurazione di default, i container Docker non saranno in grado di risolvere i domini DNS, questo significa che Interner è completamente irraggiungibile dai container.
Per risolvere questo problema, nella cartella root dove è presente il Dockerfile eseguire il seguente comando che configura docker ad utilizzare la stessa rete dell'host locale:

**docker build --network=host -t my-image-name .**


