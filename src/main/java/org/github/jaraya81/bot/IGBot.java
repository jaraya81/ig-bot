package org.github.jaraya81.bot;

import org.github.jaraya81.exception.BotException;
import org.github.jaraya81.service.DataService;

public interface IGBot {
    IGBot addDataService(DataService ds) throws BotException;

    IGBot init() throws BotException;

    IGBot open() throws BotException;

    IGBot login() throws BotException;

    IGBot follow() throws BotException;

    IGBot likes() throws BotException;

    IGBot unfollow() throws BotException;

    IGBot unlikes() throws BotException;

    IGBot close() throws BotException;

    IGBot end() throws BotException;

    IGBot pause(int seconds) throws BotException;

    IGBot pause(int seconds, int plusRandom) throws BotException;
}
