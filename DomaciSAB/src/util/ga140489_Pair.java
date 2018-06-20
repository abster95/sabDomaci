/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import operations.PackageOperations.Pair;

/**
 *
 * @author Abi
 */
public class ga140489_Pair<A,B> implements Pair<A,B> {
       private A param1;
    private B param2;
    
    public ga140489_Pair(A param1, B param2){
        this.param1=param1;
        this.param2=param2;
    }
    
    @Override
    public A getFirstParam() {
        return param1;
    }

    @Override
    public B getSecondParam() {
        return param2;
    }
}
