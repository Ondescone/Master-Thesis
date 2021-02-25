# MSc-Thesis

Installazione per la versione 20.04 di Ubuntu e setup di CORE:

CORE fornisce uno script per automatizzare l'installazione dell'ambiente virtuale. Il processo di installazione automatico è indicato al seguente [link](https://coreemu.github.io/core/install.html#automated-install). In breve i comandi da eseguire sono:
* git clone https://github.com/coreemu/core.git
*  cd core
* ./install.sh 

Dopo che l'installazione è avvenuta con successo si dovrebbe essere in grado di eseguire core-daemon e core-pygui. Il demone core può essere avviato direttamente da command line, utile per vedere i log generati: **sudo core-daemon --ovs**. Il flag *--ovs* permette di creare tutti gli switch Ethernet istanziati sull'ambiente CORE usando openvswitch, eseguito non nel contesto del singolo nodo. Questo significa che nella macchina host su cui è eseguito CORE è possibile vedere tutte le interfacce che definiscono la rete. Per interconnettere le interfacce dei nodi vengono utilizzate coppie di *veth*.
Installazione diretta di openvswitch:
* sudo apt update
* sudo apt upgrade
* sudo apt install openvswitch-switch

Si ha in dotazione *ovs-vsctl*, l'utility che permette di aggiornare ed interrogare il demone che implementa lo switch ovs (*ovs-vswitchd*). Utilizzando il comando **ovs-vsctl show** si ha una overview del contenuto del database di ovs, che mostra i bridge creati, di solito denominati con il prefisso *b.* seguiti da una notazione decimale puntata. Potenzialmente tutti i bridge listati possono essere connessi al controller ONOS. Il protocollo OpenFlow non è abilitato di default e per configurare un bridge (e.g b.1.2) per supportare le versioni del protocollo è necessario lanciare il comando **ovs-vsctl set bridge b.1.2 protocols=OpenFlow10,OpenFlow12,Open-Flow13,OpenFlow14,OpenFlow15** (in questo modo si abilitano tutte e 5 le versioni del protocollo). 
