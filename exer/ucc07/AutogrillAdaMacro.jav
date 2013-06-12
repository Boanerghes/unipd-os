public class Autogrill {
  private class ClienteBanco extends ADAThread {
    protected int id;
    protected String cassa;

    public ClienteBanco (int id, String cassa) {
      super ("CB" + id);
      this.id = id;
      this.cassa = cassa;
    }

    public void run() {
      Util.rsleep(1000, 10000);
      entryCall(PRICE, cassa, pago_A, T);
    }
  }

  private class ClienteNegozio extends ADAThread {
    protected int id;
    protected String cassa;

    public ClienteNegozio (int id, String cassa) {
      super ("CB" + id);
      this.id = id;
      this.cassa = cassa;
    }

    public void run() {
      Util.rsleep(1000, 10000);
      entryCall(PRICE, cassa, pago_B);
    }
  }

  private class Cassa extends ADAThread {
    protected String name;
    protected boolean priority;
    protected int totA;
    protected int totB;

    public Cassa (String name) {
      this.name = name;
    }

    public void run() {
      Select req = new Select();
      req.add ( when(!priority || entryCount()) => "pago_A" [in: int price] { 
        if (entryCount("pago_B") > MAXB) priority = true;
        else priority = false;
        totA += price;
      } );

      req.add( "pago_B" [in: int price] {
        if (entryCount("pago_B") < MINB) priority = false;
        totB += price;
      });

      while (true) {
        req.accept();
      }
    }
  }
}