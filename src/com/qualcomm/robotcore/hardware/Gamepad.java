/*     */ package com.qualcomm.robotcore.hardware;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.os.Build.VERSION;
/*     */ import android.view.InputDevice;
/*     */ import android.view.KeyEvent;
/*     */ import android.view.MotionEvent;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.robocol.RobocolParsable.MsgType;
/*     */ import com.qualcomm.robotcore.robocol.RobocolParsableBase;
/*     */ import com.qualcomm.robotcore.util.Range;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.AbstractMap.SimpleEntry;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Gamepad
/*     */   extends RobocolParsableBase
/*     */ {
/*     */   public static final int ID_UNASSOCIATED = -1;
/*  93 */   public float left_stick_x = 0.0F;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  98 */   public float left_stick_y = 0.0F;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 103 */   public float right_stick_x = 0.0F;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 108 */   public float right_stick_y = 0.0F;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 113 */   public boolean dpad_up = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 118 */   public boolean dpad_down = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 123 */   public boolean dpad_left = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 128 */   public boolean dpad_right = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 133 */   public boolean a = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 138 */   public boolean b = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 143 */   public boolean x = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 148 */   public boolean y = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 155 */   public boolean guide = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 160 */   public boolean start = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 165 */   public boolean back = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 170 */   public boolean left_bumper = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 175 */   public boolean right_bumper = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 180 */   public boolean left_stick_button = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 185 */   public boolean right_stick_button = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 190 */   public float left_trigger = 0.0F;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 195 */   public float right_trigger = 0.0F;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 200 */   public byte user = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 205 */   public int id = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 210 */   public long timestamp = 0L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 216 */   protected float dpadThreshold = 0.2F;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 222 */   protected float joystickDeadzone = 0.2F;
/*     */   
/*     */   private static final short PAYLOAD_SIZE = 42;
/*     */   
/*     */   private static final short BUFFER_SIZE = 47;
/*     */   
/*     */   private static final byte ROBOCOL_VERSION = 2;
/*     */   
/*     */   private static final float MAX_MOTION_RANGE = 1.0F;
/*     */   
/*     */   private final GamepadCallback callback;
/*     */   
/* 234 */   private static Set<Integer> gameControllerDeviceIdCache = new HashSet();
/*     */   
/*     */ 
/* 237 */   private static Set<DeviceId> deviceWhitelist = null;
/*     */   
/*     */ 
/*     */   private static class DeviceId
/*     */     extends AbstractMap.SimpleEntry<Integer, Integer>
/*     */   {
/*     */     private static final long serialVersionUID = -6429575391769944899L;
/*     */     
/*     */ 
/*     */     public DeviceId(int vendorId, int productId)
/*     */     {
/* 248 */       super(Integer.valueOf(productId));
/*     */     }
/*     */     
/*     */     public int getVendorId()
/*     */     {
/* 253 */       return ((Integer)getKey()).intValue();
/*     */     }
/*     */     
/*     */     public int getProductId()
/*     */     {
/* 258 */       return ((Integer)getValue()).intValue();
/*     */     }
/*     */   }
/*     */   
/*     */   public Gamepad()
/*     */   {
/* 264 */     this(null);
/*     */   }
/*     */   
/*     */   public Gamepad(GamepadCallback callback) {
/* 268 */     this.callback = callback;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void copy(Gamepad gamepad)
/*     */     throws RobotCoreException
/*     */   {
/* 279 */     fromByteArray(gamepad.toByteArray());
/*     */   }
/*     */   
/*     */ 
/*     */   public void reset()
/*     */   {
/*     */     try
/*     */     {
/* 287 */       copy(new Gamepad());
/*     */     }
/*     */     catch (RobotCoreException e) {
/* 290 */       RobotLog.e("Gamepad library in an invalid state");
/* 291 */       throw new IllegalStateException("Gamepad library in an invalid state");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setJoystickDeadzone(float deadzone)
/*     */   {
/* 300 */     if ((deadzone < 0.0F) || (deadzone > 1.0F)) {
/* 301 */       throw new IllegalArgumentException("deadzone cannot be greater than max joystick value");
/*     */     }
/*     */     
/* 304 */     this.joystickDeadzone = deadzone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update(MotionEvent event)
/*     */   {
/* 313 */     this.id = event.getDeviceId();
/* 314 */     this.timestamp = event.getEventTime();
/*     */     
/* 316 */     this.left_stick_x = cleanMotionValues(event.getAxisValue(0));
/* 317 */     this.left_stick_y = cleanMotionValues(event.getAxisValue(1));
/* 318 */     this.right_stick_x = cleanMotionValues(event.getAxisValue(11));
/* 319 */     this.right_stick_y = cleanMotionValues(event.getAxisValue(14));
/* 320 */     this.left_trigger = event.getAxisValue(17);
/* 321 */     this.right_trigger = event.getAxisValue(18);
/* 322 */     this.dpad_down = (event.getAxisValue(16) > this.dpadThreshold);
/* 323 */     this.dpad_up = (event.getAxisValue(16) < -this.dpadThreshold);
/* 324 */     this.dpad_right = (event.getAxisValue(15) > this.dpadThreshold);
/* 325 */     this.dpad_left = (event.getAxisValue(15) < -this.dpadThreshold);
/*     */     
/* 327 */     callCallback();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update(KeyEvent event)
/*     */   {
/* 336 */     this.id = event.getDeviceId();
/* 337 */     this.timestamp = event.getEventTime();
/*     */     
/* 339 */     int key = event.getKeyCode();
/* 340 */     if (key == 19) { this.dpad_up = pressed(event);
/* 341 */     } else if (key == 20) { this.dpad_down = pressed(event);
/* 342 */     } else if (key == 22) { this.dpad_right = pressed(event);
/* 343 */     } else if (key == 21) { this.dpad_left = pressed(event);
/* 344 */     } else if (key == 96) { this.a = pressed(event);
/* 345 */     } else if (key == 97) { this.b = pressed(event);
/* 346 */     } else if (key == 99) { this.x = pressed(event);
/* 347 */     } else if (key == 100) { this.y = pressed(event);
/* 348 */     } else if (key == 110) { this.guide = pressed(event);
/* 349 */     } else if (key == 108) { this.start = pressed(event);
/* 350 */     } else if (key == 109) { this.back = pressed(event);
/* 351 */     } else if (key == 103) { this.right_bumper = pressed(event);
/* 352 */     } else if (key == 102) { this.left_bumper = pressed(event);
/* 353 */     } else if (key == 106) { this.left_stick_button = pressed(event);
/* 354 */     } else if (key == 107) { this.right_stick_button = pressed(event);
/*     */     }
/* 356 */     callCallback();
/*     */   }
/*     */   
/*     */   public RobocolParsable.MsgType getRobocolMsgType()
/*     */   {
/* 361 */     return RobocolParsable.MsgType.GAMEPAD;
/*     */   }
/*     */   
/*     */   public byte[] toByteArray()
/*     */     throws RobotCoreException
/*     */   {
/* 367 */     ByteBuffer buffer = getWriteBuffer(42);
/*     */     try
/*     */     {
/* 370 */       int buttons = 0;
/*     */       
/* 372 */       buffer.put((byte)2);
/* 373 */       buffer.putInt(this.id);
/* 374 */       buffer.putLong(this.timestamp).array();
/* 375 */       buffer.putFloat(this.left_stick_x).array();
/* 376 */       buffer.putFloat(this.left_stick_y).array();
/* 377 */       buffer.putFloat(this.right_stick_x).array();
/* 378 */       buffer.putFloat(this.right_stick_y).array();
/* 379 */       buffer.putFloat(this.left_trigger).array();
/* 380 */       buffer.putFloat(this.right_trigger).array();
/*     */       
/* 382 */       buttons = (buttons << 1) + (this.left_stick_button ? 1 : 0);
/* 383 */       buttons = (buttons << 1) + (this.right_stick_button ? 1 : 0);
/* 384 */       buttons = (buttons << 1) + (this.dpad_up ? 1 : 0);
/* 385 */       buttons = (buttons << 1) + (this.dpad_down ? 1 : 0);
/* 386 */       buttons = (buttons << 1) + (this.dpad_left ? 1 : 0);
/* 387 */       buttons = (buttons << 1) + (this.dpad_right ? 1 : 0);
/* 388 */       buttons = (buttons << 1) + (this.a ? 1 : 0);
/* 389 */       buttons = (buttons << 1) + (this.b ? 1 : 0);
/* 390 */       buttons = (buttons << 1) + (this.x ? 1 : 0);
/* 391 */       buttons = (buttons << 1) + (this.y ? 1 : 0);
/* 392 */       buttons = (buttons << 1) + (this.guide ? 1 : 0);
/* 393 */       buttons = (buttons << 1) + (this.start ? 1 : 0);
/* 394 */       buttons = (buttons << 1) + (this.back ? 1 : 0);
/* 395 */       buttons = (buttons << 1) + (this.left_bumper ? 1 : 0);
/* 396 */       buttons = (buttons << 1) + (this.right_bumper ? 1 : 0);
/* 397 */       buffer.putInt(buttons);
/*     */       
/* 399 */       buffer.put(this.user);
/*     */     } catch (BufferOverflowException e) {
/* 401 */       RobotLog.logStacktrace(e);
/*     */     }
/*     */     
/* 404 */     return buffer.array();
/*     */   }
/*     */   
/*     */   public void fromByteArray(byte[] byteArray) throws RobotCoreException
/*     */   {
/* 409 */     if (byteArray.length < 47) {
/* 410 */       throw new RobotCoreException("Expected buffer of at least 47 bytes, received " + byteArray.length);
/*     */     }
/*     */     
/* 413 */     ByteBuffer byteBuffer = getReadBuffer(byteArray);
/*     */     
/* 415 */     int buttons = 0;
/*     */     
/* 417 */     byte version = byteBuffer.get();
/*     */     
/*     */ 
/* 420 */     if (version >= 1) {
/* 421 */       this.id = byteBuffer.getInt();
/* 422 */       this.timestamp = byteBuffer.getLong();
/* 423 */       this.left_stick_x = byteBuffer.getFloat();
/* 424 */       this.left_stick_y = byteBuffer.getFloat();
/* 425 */       this.right_stick_x = byteBuffer.getFloat();
/* 426 */       this.right_stick_y = byteBuffer.getFloat();
/* 427 */       this.left_trigger = byteBuffer.getFloat();
/* 428 */       this.right_trigger = byteBuffer.getFloat();
/*     */       
/* 430 */       buttons = byteBuffer.getInt();
/* 431 */       this.left_stick_button = ((buttons & 0x4000) != 0);
/* 432 */       this.right_stick_button = ((buttons & 0x2000) != 0);
/* 433 */       this.dpad_up = ((buttons & 0x1000) != 0);
/* 434 */       this.dpad_down = ((buttons & 0x800) != 0);
/* 435 */       this.dpad_left = ((buttons & 0x400) != 0);
/* 436 */       this.dpad_right = ((buttons & 0x200) != 0);
/* 437 */       this.a = ((buttons & 0x100) != 0);
/* 438 */       this.b = ((buttons & 0x80) != 0);
/* 439 */       this.x = ((buttons & 0x40) != 0);
/* 440 */       this.y = ((buttons & 0x20) != 0);
/* 441 */       this.guide = ((buttons & 0x10) != 0);
/* 442 */       this.start = ((buttons & 0x8) != 0);
/* 443 */       this.back = ((buttons & 0x4) != 0);
/* 444 */       this.left_bumper = ((buttons & 0x2) != 0);
/* 445 */       this.right_bumper = ((buttons & 0x1) != 0);
/*     */     }
/*     */     
/*     */ 
/* 449 */     if (version >= 2) {
/* 450 */       this.user = byteBuffer.get();
/*     */     }
/*     */     
/* 453 */     callCallback();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean atRest()
/*     */   {
/* 461 */     return (this.left_stick_x == 0.0F) && (this.left_stick_y == 0.0F) && (this.right_stick_x == 0.0F) && (this.right_stick_y == 0.0F) && (this.left_trigger == 0.0F) && (this.right_trigger == 0.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String type()
/*     */   {
/* 472 */     return "Standard";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 481 */     String buttons = new String();
/* 482 */     if (this.dpad_up) buttons = buttons + "dpad_up ";
/* 483 */     if (this.dpad_down) buttons = buttons + "dpad_down ";
/* 484 */     if (this.dpad_left) buttons = buttons + "dpad_left ";
/* 485 */     if (this.dpad_right) buttons = buttons + "dpad_right ";
/* 486 */     if (this.a) buttons = buttons + "a ";
/* 487 */     if (this.b) buttons = buttons + "b ";
/* 488 */     if (this.x) buttons = buttons + "x ";
/* 489 */     if (this.y) buttons = buttons + "y ";
/* 490 */     if (this.guide) buttons = buttons + "guide ";
/* 491 */     if (this.start) buttons = buttons + "start ";
/* 492 */     if (this.back) buttons = buttons + "back ";
/* 493 */     if (this.left_bumper) buttons = buttons + "left_bumper ";
/* 494 */     if (this.right_bumper) buttons = buttons + "right_bumper ";
/* 495 */     if (this.left_stick_button) buttons = buttons + "left stick button ";
/* 496 */     if (this.right_stick_button) { buttons = buttons + "right stick button ";
/*     */     }
/* 498 */     return String.format("ID: %2d user: %2d lx: % 1.2f ly: % 1.2f rx: % 1.2f ry: % 1.2f lt: %1.2f rt: %1.2f %s", new Object[] { Integer.valueOf(this.id), Byte.valueOf(this.user), Float.valueOf(this.left_stick_x), Float.valueOf(this.left_stick_y), Float.valueOf(this.right_stick_x), Float.valueOf(this.right_stick_y), Float.valueOf(this.left_trigger), Float.valueOf(this.right_trigger), buttons });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float cleanMotionValues(float number)
/*     */   {
/* 509 */     if ((number < this.joystickDeadzone) && (number > -this.joystickDeadzone)) { return 0.0F;
/*     */     }
/*     */     
/* 512 */     if (number > 1.0F) return 1.0F;
/* 513 */     if (number < -1.0F) { return -1.0F;
/*     */     }
/*     */     
/* 516 */     if (number > 0.0F) {
/* 517 */       number = (float)Range.scale(number, this.joystickDeadzone, 1.0D, 0.0D, 1.0D);
/*     */     } else {
/* 519 */       number = (float)Range.scale(number, -this.joystickDeadzone, -1.0D, 0.0D, -1.0D);
/*     */     }
/* 521 */     return number;
/*     */   }
/*     */   
/*     */   protected boolean pressed(KeyEvent event) {
/* 525 */     return event.getAction() == 0;
/*     */   }
/*     */   
/*     */   protected void callCallback() {
/* 529 */     if (this.callback != null) { this.callback.gamepadChanged(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void enableWhitelistFilter(int vendorId, int productId)
/*     */   {
/* 544 */     if (deviceWhitelist == null) {
/* 545 */       deviceWhitelist = new HashSet();
/*     */     }
/* 547 */     deviceWhitelist.add(new DeviceId(vendorId, productId));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void clearWhitelistFilter()
/*     */   {
/* 554 */     deviceWhitelist = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @TargetApi(19)
/*     */   public static synchronized boolean isGamepadDevice(int deviceId)
/*     */   {
/* 566 */     if (gameControllerDeviceIdCache.contains(Integer.valueOf(deviceId))) {
/* 567 */       return true;
/*     */     }
/*     */     
/* 570 */     gameControllerDeviceIdCache = new HashSet();
/* 571 */     int[] deviceIds = InputDevice.getDeviceIds();
/* 572 */     for (int id : deviceIds) {
/* 573 */       InputDevice device = InputDevice.getDevice(id);
/*     */       
/* 575 */       int source = device.getSources();
/* 576 */       if (((source & 0x401) == 1025) || ((source & 0x1000010) == 16777232))
/*     */       {
/*     */ 
/* 579 */         if (Build.VERSION.SDK_INT >= 19)
/*     */         {
/*     */ 
/* 582 */           if ((deviceWhitelist == null) || (deviceWhitelist.contains(new DeviceId(device.getVendorId(), device.getProductId()))))
/*     */           {
/* 584 */             gameControllerDeviceIdCache.add(Integer.valueOf(id));
/*     */           }
/*     */         } else {
/* 587 */           gameControllerDeviceIdCache.add(Integer.valueOf(id));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 593 */     if (gameControllerDeviceIdCache.contains(Integer.valueOf(deviceId))) {
/* 594 */       return true;
/*     */     }
/*     */     
/* 597 */     return false;
/*     */   }
/*     */   
/*     */   public static abstract interface GamepadCallback
/*     */   {
/*     */     public abstract void gamepadChanged(Gamepad paramGamepad);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\Gamepad.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */