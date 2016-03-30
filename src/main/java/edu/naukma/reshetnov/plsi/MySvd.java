package edu.naukma.reshetnov.plsi;


import org.apache.mahout.math.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Component
@RestController
public class MySvd {

  @RequestMapping("/")
  public void doSmth() {
    System.out.println("Hi!");
    Vector v = new DenseVector(new double[]{3.0, 4.0});
    Matrix m = new RandomTrinaryMatrix(20,20);
    System.out.println(m);
    SingularValueDecomposition svd = new SingularValueDecomposition(m);
    double[] singularValues = svd.getSingularValues();
    System.out.println("Singulars: " + Arrays.toString(singularValues));
  }

  public static void main(String[] args) {
    new MySvd().doSmth();
  }
}
