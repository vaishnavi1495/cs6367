package org.pitest.mutationtest.build.intercept.kotlin;

import java.util.Collection;

import org.pitest.bytecode.analysis.ClassTree;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.prelude.Prelude;
import org.pitest.mutationtest.build.InterceptorType;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationDetails;

/**
 * Quick dirty hack to filter out some of the junk mutations
 * created for kotlin classes. These are mutations in autogenerated
 * methods not created by the programmer, such as the copy method
 * in data classes.
 * 
 * For the amount assume that anything on line 0 of a kotlin class
 * is autogenerated. This won't catch everything and will probably
 * sometimes trigger when it shouldn't, but overall is a big
 * improvement for very little effort.
 *
 */
public class KotlinFilter implements MutationInterceptor  {


  @Override
  public Collection<MutationDetails> intercept(
      Collection<MutationDetails> mutations, Mutater m) {
    return FCollection.filter(mutations, Prelude.not(isKotlinJunkMutation()));
  }
  
  @Override
  public InterceptorType type() {
    return InterceptorType.FILTER;
  }

  @Override
  public void begin(ClassTree clazz) {
    // noop  
  }

  @Override
  public void end() {
    // noop   
  }
  
  private static F<MutationDetails, Boolean> isKotlinJunkMutation() {
    return new  F<MutationDetails, Boolean>() {
      @Override
      public Boolean apply(MutationDetails a) {
        return a.getFilename().toLowerCase().endsWith(".kt") && a.getLineNumber() == 0;
      }
    };
  }
}
