#### 如何在标量,向量,矩阵与张量中使用int并保证不丢失精度.

利用`Float.intBitsToFloat`与`Float.floatToIntBits`可以实现int与float转换,并且防止精度丢失.