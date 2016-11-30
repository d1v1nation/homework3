package io.github.d1v1nation.fourtwenty;

/**
 * @author d1v1nation (catanaut@gmail.com)
 *         <p>
 *         30.11.16 of fourtwenty | io.github.d1v1nation.fourtwenty
 */
public class Unit {
    private static Unit ourInstance = new Unit();

    public static Unit getInstance() {
        return ourInstance;
    }

    private Unit() {
    }
}
