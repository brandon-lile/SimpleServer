/*
 * Copyright (c) 2010 SimpleServer authors (see CONTRIBUTORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package simpleserver.command;

import simpleserver.Color;
import simpleserver.Player;
import simpleserver.config.xml.Config;
import simpleserver.config.xml.Group;
import simpleserver.message.Chat;
import simpleserver.message.GroupChat;

public class GroupSayCommand extends MessageCommand implements PlayerCommand {

  String chatMessage;

  public GroupSayCommand() {
    super("group [GROUP] MESSAGE", "talk to the specified or your group (use id)");
  }

  @Override
  protected Chat getMessageInstance(Player sender, String message) {
    // TODO: make it possible to use group name(prefix)

    Group group = sender.getGroup();
    chatMessage = message;
    Config config = sender.getServer().config;

    String[] arguments = extractArguments(message);
    if (arguments.length > 1) {
      try {
        int groupId = Integer.parseInt(arguments[0]);
        if (config.groups.contains(groupId)) {
          group = config.groups.get(groupId);
          chatMessage = extractArgument(message);
        } else {
          throw new NumberFormatException("Group doesn't exist");
        }
      } catch (NumberFormatException e) {
        sender.addTMessage(Color.RED, "Invalid group ID");
        return null;
      }
    }

    return new GroupChat(sender, group);
  }

  @Override
  protected String extractMessage(String message) {
    return extractArgument(chatMessage);
  }

}
