package org.objectweb.asm.tree;

import org.objectweb.asm.MethodVisitor;

import java.util.Map;

public class LdcInsnNode extends AbstractInsnNode {
  public Object cst;
  
  public LdcInsnNode(Object value) {
    super(18);
    this.cst = value;
  }
  
  public int getType() { return 9; }
  
  public void accept(MethodVisitor methodVisitor) {
    methodVisitor.visitLdcInsn(this.cst);
    acceptAnnotations(methodVisitor);
  }
  
  public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) { return (new LdcInsnNode(this.cst)).cloneAnnotations(this); }
}
